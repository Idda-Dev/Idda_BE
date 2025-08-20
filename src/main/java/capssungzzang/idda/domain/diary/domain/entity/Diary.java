package capssungzzang.idda.domain.diary.domain.entity;


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
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private String emotion;

    @Builder
    public Diary(Member member, String content, String emotion) {
        this.member = member;
        this.content = content;
        this.emotion = emotion;
    }

}
