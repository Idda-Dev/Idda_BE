package capssungzzang._dayscottonball.domain.post.domain.entity;

import capssungzzang._dayscottonball.domain.member.domain.entity.Member;
import capssungzzang._dayscottonball.domain.mission.domain.entity.Mission;
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
public class VerificationPost extends Post {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mission_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Mission mission;

    @Column(length = 2048)
    private String photoUrl;

    @Column
    private String location;

    @Builder(builderMethodName = "verificationBuilder")
    public VerificationPost(Member member, String title, String content,
                            String photoUrl, String location, Mission mission) {
        super(member, title, content);
        this.photoUrl = photoUrl;
        this.location = location;
        this.mission = mission;
    }
}
