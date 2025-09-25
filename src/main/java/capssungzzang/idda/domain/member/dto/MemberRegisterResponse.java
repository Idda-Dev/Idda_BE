package capssungzzang.idda.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRegisterResponse {
    private Long memberId;
    private String nickname;
    private boolean newMember;
    private boolean levelAssigned;
}
