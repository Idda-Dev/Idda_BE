package capssungzzang.idda.domain.survey.domain.entity;

import capssungzzang.idda.domain.member.domain.entity.Member;
import capssungzzang.idda.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Survey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column
    private Integer question1;

    @Column
    private Integer question2;

    @Column
    private Integer question3;

    @Column
    private Integer question4;

    @Column
    private Integer question5;

    @Column
    private Integer question6;

    @Builder
    public Survey(Member member) {
        this.member = member;
    }

    public void updateQuestion(int index, Integer answer) {
        switch (index) {
            case 1 -> this.question1 = answer;
            case 2 -> this.question2 = answer;
            case 3 -> this.question3 = answer;
            case 4 -> this.question4 = answer;
            case 5 -> this.question5 = answer;
            case 6 -> this.question6 = answer;
        }
    }

}
