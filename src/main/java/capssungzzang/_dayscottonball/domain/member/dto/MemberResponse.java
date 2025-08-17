package capssungzzang._dayscottonball.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponse {
    private Long memberId;
    private String nickname;
    private int candy;
    private int level;
    private int successCount;
    private int totalCount;
}
