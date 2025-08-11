package capssungzzang._dayscottonball.domain.mission.domain.entity;

import capssungzzang._dayscottonball.domain.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Band band;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private boolean isAchieved;

    @Column
    private String tag;

    public enum Band { A, B, C, D, E }

    @Builder
    public Mission(Member member, String content, Band band, int level, boolean isAchieved, String tag) {
        this.member = member;
        this.content = content;
        this.band = band;
        this.level = level;
        this.isAchieved = isAchieved;
        this.tag = tag;
    }
}
