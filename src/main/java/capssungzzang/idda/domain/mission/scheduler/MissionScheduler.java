package capssungzzang.idda.domain.mission.scheduler;

import capssungzzang.idda.domain.mission.application.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MissionScheduler {

    private final MissionService missionService;

    @Scheduled(cron="0 0 0 * * *", zone="Asia/Seoul")
    public void runDaily() { missionService.generateMission(); }
}
