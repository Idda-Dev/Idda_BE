package capssungzzang.idda.domain.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationPostCreateResponse {
    private Long postId;
    private boolean levelUp;
    private int level;
}
