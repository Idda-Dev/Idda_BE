package capssungzzang._dayscottonball.domain.post.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationPost extends Post {
    @Column(length = 2048)
    private String photoUrl;

    @Column
    private String location;
}
