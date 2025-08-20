package capssungzzang._dayscottonball.domain.mission.domain.repository;

import capssungzzang._dayscottonball.domain.level.domain.entity.difficulty.Difficulty;
import capssungzzang._dayscottonball.domain.mission.domain.entity.SpareMission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpareMissionRepository extends JpaRepository<SpareMission, Long> {
    Optional<SpareMission> findByLevelAndDifficulty(int level, Difficulty difficulty);
}
