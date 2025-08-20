package capssungzzang.idda.domain.mission.dto;

import capssungzzang.idda.domain.level.domain.entity.difficulty.Difficulty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MissionResponse {
    private Long missionId;
    private String content;
    private String missionComment;
    private int level;
    private Difficulty difficulty;
}
