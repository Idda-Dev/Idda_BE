package capssungzzang.idda.domain.member.domain.entity;

import capssungzzang.idda.domain.level.domain.entity.difficulty.Difficulty;
import capssungzzang.idda.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_member_level", columnNames = {"member_id","level"}))
public class MemberMissionProgress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private int level;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Difficulty difficulty;

    @Column(nullable = false)
    private int successCount;

    @Column(nullable = false)
    private boolean completed;

    @Builder
    private MemberMissionProgress(Member member, int level, Difficulty difficulty, int successCount, boolean completed) {
        this.member = member;
        this.level = level;
        this.difficulty = difficulty;
        this.successCount = successCount;
        this.completed = completed;
    }

}
