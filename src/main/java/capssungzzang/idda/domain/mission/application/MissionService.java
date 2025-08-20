package capssungzzang.idda.domain.mission.application;

import capssungzzang.idda.domain.mission.dto.MissionAchievementResponse;
import capssungzzang.idda.domain.mission.dto.MissionResponse;

import java.time.LocalDate;

public interface MissionService {
    MissionResponse getMission(Long memberId, LocalDate date);
    MissionAchievementResponse getAchievementDates(Long userId, int year, int month);
    void generateMission();
    MissionResponse refreshMission(Long memberId);
}
