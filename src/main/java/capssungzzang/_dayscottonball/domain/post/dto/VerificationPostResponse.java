package capssungzzang._dayscottonball.domain.post.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VerificationPostResponse {
    private Long postId;
    private Long memberId;
    private Long missionId;
    private String profileImageUrl;
    private String nickname;
    private String title;
    private String content;
    private String photoUrl;
    private String location;
    private long hearts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
