package capssungzzang._dayscottonball.domain.member.domain.entity;

import capssungzzang._dayscottonball.domain.comment.domain.entity.Comment;
import capssungzzang._dayscottonball.domain.diary.domain.entity.Diary;
import capssungzzang._dayscottonball.domain.heart.domain.entity.Heart;
import capssungzzang._dayscottonball.domain.letter.domain.entity.Letter;
import capssungzzang._dayscottonball.domain.mission.domain.entity.Mission;
import capssungzzang._dayscottonball.domain.post.domain.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Letter> letters = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Diary> diaries = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Mission> missions = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Member(String nickname, int candy, int level, Band band) {
        this.nickname = nickname;
        this.candy = candy;
        this.level = level;
        this.band = band;
    }

}
