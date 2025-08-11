package capssungzzang._dayscottonball.domain.member.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String nickname;

    @Column(nullable = false)
    private int candy;

    @Column(nullable = false)
    private int level; // 1~10(임시)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private Band band; // A~E(임시)

    public enum Band { A, B, C, D, E }

    @Builder
    public Member(String nickname, int candy, int difficulty, Integer level, Band band) {
        this.nickname = nickname;
        this.candy = candy;
        this.level = level;
        this.band  = band;
    }

}
