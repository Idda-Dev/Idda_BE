package capssungzzang._dayscottonball.domain.member.domain.entity;

import capssungzzang._dayscottonball.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String nickname;

    @Column(nullable = false)
    private int candy;

    @Column(length = 2048)
    private String profileImageUrl;

    @Column
    private String location;

    @Builder
    public Member(String nickname, int candy,  String profileImageUrl, String location) {
        this.nickname = nickname;
        this.candy = candy;
        this.profileImageUrl = profileImageUrl;
        this.location = location;
    }

}
