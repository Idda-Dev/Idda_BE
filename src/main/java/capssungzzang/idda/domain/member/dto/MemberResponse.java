package capssungzzang.idda.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberResponse {
    private Long memberId;
    private String nickname;
    private int candy;
    private String profileImageUrl;
    private String location;
    private int level;
    private LocalDateTime createdAt;
}
