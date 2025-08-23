package capssungzzang.idda.domain.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationPostDailyResponse {
    private Long postId;
    private String title;
    private String content;
    private String photoUrl;
}
