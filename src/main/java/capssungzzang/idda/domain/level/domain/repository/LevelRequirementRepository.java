package capssungzzang.idda.domain.level.domain.repository;

import capssungzzang.idda.domain.level.domain.entity.LevelRequirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LevelRequirementRepository extends JpaRepository<LevelRequirement, Long> {
    Optional<LevelRequirement> findByLevel(int level);
}
