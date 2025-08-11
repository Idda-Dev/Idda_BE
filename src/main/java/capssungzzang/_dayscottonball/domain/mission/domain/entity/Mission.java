package capssungzzang._dayscottonball.domain.mission.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}
