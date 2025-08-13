package capssungzzang._dayscottonball.domain.mission.application;

import capssungzzang._dayscottonball.domain.member.domain.repository.MemberRepository;
import capssungzzang._dayscottonball.domain.mission.domain.entity.Mission;
import capssungzzang._dayscottonball.domain.mission.domain.repository.MissionRepository;
import capssungzzang._dayscottonball.domain.mission.dto.MissionAchievementResponse;
import capssungzzang._dayscottonball.domain.mission.dto.MissionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;
    private final MemberRepository memberRepository;

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
}
