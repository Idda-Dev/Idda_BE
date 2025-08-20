package capssungzzang._dayscottonball.domain.post.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostResponse {
    private Long memberId;
    private Long postId;
    private String title;
    private String content;
    private long hearts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
