package capssungzzang._dayscottonball.domain.level.domain.entity;

import capssungzzang._dayscottonball.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LevelRequirement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private int level;

    @Column(name = "easy_required", nullable = false)
    private int easyRequired;

    @Column(name = "normal_required", nullable = false)
    private int normalRequired;

    @Column(name = "hard_required", nullable = false)
    private int hardRequired;

    @Column(name = "random_mode", nullable = false)
    private boolean randomMode;

    public enum Difficulty { EASY, NORMAL, HARD }

    public int requiredFor(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> easyRequired;
            case NORMAL -> normalRequired;
            case HARD -> hardRequired;
        };
    }
}
