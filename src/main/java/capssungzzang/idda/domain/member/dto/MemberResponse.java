package capssungzzang.idda.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponse {
    private Long memberId;
    private String nickname;
    private int candy;
    private String profileImageUrl;
    private String location;
    private int level;
    private int successCount;
    private int totalCount;
}
