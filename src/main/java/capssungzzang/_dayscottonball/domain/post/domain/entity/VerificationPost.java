package capssungzzang._dayscottonball.domain.post.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 자식: 인증글 (부모 PK를 그대로 사용)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationPost extends Post {
    @Column(length = 2048)
    private String photoUrl;

    @Column
    private String location;
}
