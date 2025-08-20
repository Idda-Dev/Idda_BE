package capssungzzang.idda.domain.mission.api;


import capssungzzang.idda.domain.mission.application.MissionService;
import capssungzzang.idda.domain.mission.dto.MissionAchievementResponse;
import capssungzzang.idda.domain.mission.dto.MissionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @GetMapping("/users/{userId}/missions")
    public ResponseEntity<MissionResponse> getMission(@PathVariable("userId") Long memberId,
                                                      @RequestParam("date") LocalDate date) {
        MissionResponse response = missionService.getMission(memberId, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}/missions/achievements")
    public ResponseEntity<MissionAchievementResponse> getAchievementDays(
            @PathVariable("userId") Long userId,
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ) {
        MissionAchievementResponse response = missionService.getAchievementDates(userId, year, month);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{userId}/missions/refresh")
    public ResponseEntity<MissionResponse> refreshMission(@PathVariable("userId") Long memberId) {
        MissionResponse response = missionService.refreshMission(memberId);
        return ResponseEntity.ok(response);
    }
}
