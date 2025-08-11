    package capssungzzang._dayscottonball.domain.post.domain.entity;

    import capssungzzang._dayscottonball.domain.comment.domain.entity.Comment;
    import capssungzzang._dayscottonball.domain.heart.domain.entity.Heart;
    import capssungzzang._dayscottonball.domain.member.domain.entity.Member;
    import jakarta.persistence.*;
    import lombok.AccessLevel;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.NoArgsConstructor;

    import java.util.ArrayList;
    import java.util.List;

    @Entity
    @Inheritance(strategy = InheritanceType.JOINED)
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public class Post {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "post_id")
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "member_id", nullable = false)
        private Member member;

        @Column(nullable=false)
        private String title;

        @Column(columnDefinition="TEXT")
        private String content;

        @Column(nullable=false)
        private int heartCount;

        @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
        private List<Comment> comments = new ArrayList<>();

        @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
        private List<Heart> hearts = new ArrayList<>();

        @Builder
        public Post(Member member, String title, String content, int heartCount) {
            this.member = member;
            this.title = title;
            this.content = content;
            this.heartCount = heartCount;
        }
    }
