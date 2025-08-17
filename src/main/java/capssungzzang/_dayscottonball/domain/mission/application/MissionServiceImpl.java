package capssungzzang._dayscottonball.domain.mission.application;

import capssungzzang._dayscottonball.domain.level.domain.entity.LevelRequirement;
import capssungzzang._dayscottonball.domain.level.domain.entity.difficulty.Difficulty;
import capssungzzang._dayscottonball.domain.level.domain.repository.LevelRequirementRepository;
import capssungzzang._dayscottonball.domain.member.domain.entity.Member;
import capssungzzang._dayscottonball.domain.member.domain.entity.MemberMissionProgress;
import capssungzzang._dayscottonball.domain.member.domain.repository.MemberMissionProgressRepository;
import capssungzzang._dayscottonball.domain.member.domain.repository.MemberRepository;
import capssungzzang._dayscottonball.domain.mission.domain.entity.Mission;
import capssungzzang._dayscottonball.domain.mission.domain.repository.MissionRepository;
import capssungzzang._dayscottonball.domain.mission.dto.MissionAchievementResponse;
import capssungzzang._dayscottonball.domain.mission.dto.MissionGenerateResponse;
import capssungzzang._dayscottonball.domain.mission.dto.MissionResponse;
import capssungzzang._dayscottonball.domain.mission.prompt.MissionPromptProvider;
import capssungzzang._dayscottonball.global.openai.client.OpenAiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Override
    public MissionResponse getMission(Long memberId, LocalDate date) {

        if (date == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "date는 필수입니다.");
        }

        if (!memberRepository.existsById(memberId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.");
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.plusDays(1).atStartOfDay();

        Mission mission = missionRepository
                .findByMemberIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(memberId, start, end)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 날짜의 미션이 없습니다."));

        MissionResponse response = new MissionResponse();

        response.setContent(mission.getContent());
        response.setTag(mission.getTag());
        response.setLevel(mission.getLevel());

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

        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDateTime start = firstDay.atStartOfDay();
        LocalDateTime end = firstDay.plusMonths(1).atStartOfDay();

        List<LocalDateTime> createdAts = missionRepository.findAchievedCreatedAtsByMemberAndMonth(userId, start, end);

        final DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;

        List<String> dates = createdAts.stream()
                .map(LocalDateTime::toLocalDate)
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

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        //당일 미션 존재 여부 검증
        if (missionRepository.existsForDay(1L, start, end)) return;

        //미션 생성은 테스트 계정(memberId=1)만
        Member member = memberRepository.findById(1L).get();

        //미션 진행 정보 조회
        MemberMissionProgress memberMissionProgress = memberMissionProgressRepository
                .findFirstByMemberIdAndCompletedFalseOrderByLevelDesc(1L).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "미션 진행 정보가 존재하지 않습니다."));

        //미션 정보 생성을 위한 레벨, 난이도
        int level = memberMissionProgress.getLevel();
        Difficulty difficulty = memberMissionProgress.getDifficulty();

        String prompt = missionPromptProvider.build(level, difficulty, member.getNickname());

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
}
