package capssungzzang.idda.domain.mission.domain.repository;

import capssungzzang.idda.domain.level.domain.entity.difficulty.Difficulty;
import capssungzzang.idda.domain.mission.domain.entity.SpareMission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpareMissionRepository extends JpaRepository<SpareMission, Long> {
    Optional<SpareMission> findByLevelAndDifficulty(int level, Difficulty difficulty);
}
