package capssungzzang._dayscottonball.domain.mission.application;

import capssungzzang._dayscottonball.domain.member.domain.repository.MemberRepository;
import capssungzzang._dayscottonball.domain.mission.domain.entity.Mission;
import capssungzzang._dayscottonball.domain.mission.domain.repository.MissionRepository;
import capssungzzang._dayscottonball.domain.mission.dto.MissionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
}
