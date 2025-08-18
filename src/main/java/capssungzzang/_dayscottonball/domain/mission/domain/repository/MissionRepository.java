package capssungzzang._dayscottonball.domain.mission.domain.repository;

import capssungzzang._dayscottonball.domain.mission.domain.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    Optional<Mission> findByMemberIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            Long memberId, LocalDateTime start, LocalDateTime end
    );

    @Query("""
        select m.createdAt
        from Mission m
        where m.member.id = :memberId
          and m.isAchieved = true
          and m.createdAt >= :start
          and m.createdAt <  :end
        """)
    List<LocalDateTime> findAchievedCreatedAtsByMemberAndMonth(
            @Param("memberId") Long memberId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
        select (count(m) > 0) from Mission m
        where m.member.id = :memberId
          and m.createdAt >= :start and m.createdAt < :end
    """)
    boolean existsForDay(@Param("memberId") Long memberId,
                         @Param("start") LocalDateTime start,
                         @Param("end") LocalDateTime end);
}
