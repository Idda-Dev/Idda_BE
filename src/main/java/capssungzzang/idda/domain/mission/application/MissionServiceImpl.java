package capssungzzang.idda.domain.mission.application;

import capssungzzang.idda.domain.level.domain.entity.difficulty.Difficulty;
import capssungzzang.idda.domain.member.domain.entity.Member;
import capssungzzang.idda.domain.member.domain.entity.MemberMissionProgress;
import capssungzzang.idda.domain.member.domain.repository.MemberMissionProgressRepository;
import capssungzzang.idda.domain.member.domain.repository.MemberRepository;
import capssungzzang.idda.domain.mission.domain.entity.Mission;
import capssungzzang.idda.domain.mission.domain.entity.SpareMission;
import capssungzzang.idda.domain.mission.domain.repository.MissionRepository;
import capssungzzang.idda.domain.mission.domain.repository.SpareMissionRepository;
import capssungzzang.idda.domain.mission.dto.MissionAchievementResponse;
import capssungzzang.idda.domain.mission.dto.MissionGenerateResponse;
import capssungzzang.idda.domain.mission.dto.MissionResponse;
import capssungzzang.idda.domain.mission.prompt.MissionPromptProvider;
import capssungzzang.idda.global.openai.client.OpenAiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;
    private final MemberRepository memberRepository;
    private final MemberMissionProgressRepository memberMissionProgressRepository;
    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper;
    private final MissionPromptProvider missionPromptProvider;
    private final SpareMissionRepository spareMissionRepository;

    @Override
    public MissionResponse getMission(Long memberId, LocalDate date) {

        if (date == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "date는 필수입니다.");
        }

        if (!memberRepository.existsById(memberId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.");
        }

        ZoneId KST = ZoneId.of("Asia/Seoul");
        LocalDateTime startUtc = date.atStartOfDay(KST)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
        LocalDateTime endUtc = startUtc.plusDays(1);

        Mission mission = missionRepository
                .findByMemberIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(memberId, startUtc, endUtc)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 날짜의 미션이 없습니다."));


        MissionResponse response = new MissionResponse();

        response.setMissionId(mission.getId());
        response.setContent(mission.getContent());
        response.setMissionComment(mission.getMissionComment());
        response.setLevel(mission.getLevel());
        response.setDifficulty(mission.getDifficulty());

        return response;
    }

    @Override
    public MissionAchievementResponse getAchievementDates(Long userId, int year, int month) {
        if (!memberRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.");
        }
        if (month < 1 || month > 12) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "month는 1~12 범위여야 합니다.");
        }

        ZoneId KST = ZoneId.of("Asia/Seoul");

        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDateTime startUtc = firstDay.atStartOfDay(KST)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
        LocalDateTime endUtc = firstDay.plusMonths(1).atStartOfDay(KST)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();

        List<LocalDateTime> createdAts = missionRepository.findAchievedCreatedAtsByMemberAndMonth(userId, startUtc, endUtc);

        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;
        List<String> dates = createdAts.stream()
                .map(tsUtc -> tsUtc.atZone(ZoneOffset.UTC)
                        .withZoneSameInstant(KST)
                        .toLocalDate())
                .distinct()
                .sorted()
                .map(fmt::format)
                .toList();


        MissionAchievementResponse response = new MissionAchievementResponse();

        response.setYear(year);
        response.setMonth(month);
        response.setDates(dates);

        return response;
    }

    @Override
    public void generateMission() {

        ZoneId KST = ZoneId.of("Asia/Seoul");

        LocalDateTime startUtc = LocalDate.now(KST)
                .atStartOfDay(KST).withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
        LocalDateTime endUtc = startUtc.plusDays(1);

        if (missionRepository.existsForDay(1L, startUtc, endUtc)) return;

        //미션 생성은 테스트 계정(memberId=1)만
        Member member = memberRepository.findById(1L).get();

        //미션 진행 정보 조회
        MemberMissionProgress memberMissionProgress = memberMissionProgressRepository
                .findFirstByMemberIdAndCompletedFalseOrderByLevelDesc(1L).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "미션 진행 정보가 존재하지 않습니다."));

        //미션 정보 생성을 위한 레벨, 난이도
        int level = memberMissionProgress.getLevel();
        Difficulty difficulty = memberMissionProgress.getDifficulty();

        String prompt = missionPromptProvider.build(level, difficulty, member.getLocation());

        //AI 호출 결과로 받은 json 내용 문자열로 저장
        String json = openAiClient.chatJson(prompt);

        //String으로 저장된 문자열 dto 변환
        MissionGenerateResponse response;
        try {
            response = objectMapper.readValue(json, MissionGenerateResponse.class);
        } catch (Exception e) {
            throw new IllegalStateException("OpenAI 응답 파싱 실패: " + json, e);
        }

        //미션 객체 생성
        Mission mission = Mission.builder()
                .member(member)
                .content(response.getContent())
                .missionComment(response.getMissionComment())
                .level(level)
                .difficulty(difficulty)
                .build();

        //미션 객체 DB 저장
        missionRepository.save(mission);
    }

    @Override
    public MissionResponse refreshMission(Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.");
        }

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end   = today.plusDays(1).atStartOfDay();

        Mission mission = missionRepository
                .findByMemberIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(memberId, start, end)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 날짜의 미션이 없습니다."));

        SpareMission spareMission = spareMissionRepository.findByLevelAndDifficulty(mission.getLevel(), mission.getDifficulty())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스페어 미션이 존재하지 않습니다.")
                );

        mission.refreshMission(spareMission.getContent(), spareMission.getMissionComment());

        MissionResponse response = new MissionResponse();

        response.setMissionId(mission.getId());
        response.setContent(mission.getContent());
        response.setMissionComment(mission.getMissionComment());
        response.setLevel(mission.getLevel());
        response.setDifficulty(mission.getDifficulty());

        return response;

    }
}
