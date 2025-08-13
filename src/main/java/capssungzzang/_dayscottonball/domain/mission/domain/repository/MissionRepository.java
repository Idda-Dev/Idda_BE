package capssungzzang._dayscottonball.domain.mission.domain.repository;

import capssungzzang._dayscottonball.domain.mission.domain.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    Optional<Mission> findByMemberIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            Long memberId, LocalDateTime start, LocalDateTime end
    );
}
