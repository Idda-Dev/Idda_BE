package capssungzzang._dayscottonball.domain.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationPostCreateRequest {
    private String title;
    private String content;
    private String photoUrl;
    private String location;
}
