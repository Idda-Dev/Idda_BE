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

    @Builder
    public Member(String nickname, int candy) {
        this.nickname = nickname;
        this.candy = candy;
    }

}
