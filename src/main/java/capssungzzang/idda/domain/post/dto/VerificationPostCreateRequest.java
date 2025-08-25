package capssungzzang.idda.domain.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationPostCreateRequest {
    private String content;
    private boolean isPublic;
}
