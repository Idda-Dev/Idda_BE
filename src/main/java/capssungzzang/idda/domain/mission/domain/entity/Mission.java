package capssungzzang.idda.domain.mission.domain.entity;

import capssungzzang.idda.domain.level.domain.entity.difficulty.Difficulty;
import capssungzzang.idda.domain.member.domain.entity.Member;
import capssungzzang.idda.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String missionComment;

    @Column(nullable = false)
    private int level;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(nullable = false)
    private boolean isAchieved;

    @Builder
    public Mission(Member member, String content, String missionComment, int level, Difficulty difficulty) {
        this.member = member;
        this.content = content;
        this.missionComment = missionComment;
        this.level = level;
        this.difficulty = difficulty;
    }

    public void refreshMission(String content, String missionComment) {
        this.content = content;
        this.missionComment = missionComment;
    }
}
