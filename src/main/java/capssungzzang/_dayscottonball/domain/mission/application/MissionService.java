package capssungzzang._dayscottonball.domain.mission.application;

import capssungzzang._dayscottonball.domain.mission.dto.MissionResponse;

import java.time.LocalDate;

public interface MissionService {
    MissionResponse getMission(Long memberId, LocalDate date);
}
