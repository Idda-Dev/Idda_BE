package capssungzzang._dayscottonball.domain.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponse {
    private Long memberId;
    private Long postId;
    private String title;
    private String content;
}
