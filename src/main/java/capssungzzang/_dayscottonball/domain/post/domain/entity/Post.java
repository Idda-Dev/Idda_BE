    package capssungzzang._dayscottonball.domain.post.domain.entity;

    import capssungzzang._dayscottonball.domain.comment.domain.entity.Comment;
    import capssungzzang._dayscottonball.domain.heart.domain.entity.Heart;
    import capssungzzang._dayscottonball.domain.member.domain.entity.Member;
    import capssungzzang._dayscottonball.global.common.entity.BaseEntity;
    import jakarta.persistence.*;
    import lombok.AccessLevel;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import org.hibernate.annotations.OnDelete;
    import org.hibernate.annotations.OnDeleteAction;

    import java.util.ArrayList;
    import java.util.List;

    @Entity
    @Inheritance(strategy = InheritanceType.JOINED)
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public class Post extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "post_id")
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "member_id", nullable = false)
        @OnDelete(action = OnDeleteAction.CASCADE)
        private Member member;

        @Column(nullable=false)
        private String title;

        @Column(columnDefinition="TEXT")
        private String content;

        @Builder
        public Post(Member member, String title, String content) {
            this.member = member;
            this.title = title;
            this.content = content;
        }

        public void updateTitle(String title) {
            this.title = title;
        }

        public void updateContent(String content) {
            this.content = content;
        }
    }
