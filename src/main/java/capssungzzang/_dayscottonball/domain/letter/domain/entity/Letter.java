package capssungzzang._dayscottonball.domain.letter.domain.entity;

import capssungzzang._dayscottonball.domain.member.domain.entity.Member;
import capssungzzang._dayscottonball.global.common.entity.BaseEntity;
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
public class Letter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder
    public Letter(Member member, String content) {
        this.member = member;
        this.content = content;
    }
}
