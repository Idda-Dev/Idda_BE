package capssungzzang.idda.domain.mission.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MissionAchievementResponse {
    private int year;
    private int month;
    private List<String> dates;
}
