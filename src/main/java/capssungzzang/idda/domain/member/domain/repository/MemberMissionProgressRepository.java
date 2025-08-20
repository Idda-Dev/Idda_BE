package capssungzzang.idda.domain.member.domain.repository;

import capssungzzang.idda.domain.member.domain.entity.MemberMissionProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberMissionProgressRepository extends JpaRepository<MemberMissionProgress, Long> {

    @Query(value = """
        SELECT COALESCE(
          (SELECT level FROM member_mission_progress
            WHERE member_id = :memberId AND completed = 0
            ORDER BY level DESC LIMIT 1),
          (SELECT MAX(level) FROM member_mission_progress
            WHERE member_id = :memberId AND completed = 1),
          1
        ) AS current_level
        """, nativeQuery = true)
    int findCurrentLevel(@Param("memberId") Long memberId);

    @Query(value = """
        WITH cur AS (
          SELECT COALESCE(
            (SELECT level FROM member_mission_progress
              WHERE member_id = :memberId AND completed = 0 ORDER BY level DESC LIMIT 1),
            (SELECT MAX(level) FROM member_mission_progress
              WHERE member_id = :memberId AND completed = 1),
            1
          ) AS lvl
        )
        SELECT
          CASE
            WHEN p.id IS NULL THEN 0
            WHEN p.difficulty = 'EASY'   THEN p.success_count
            WHEN p.difficulty = 'NORMAL' THEN lr.easy_required + p.success_count
            WHEN p.difficulty = 'HARD'   THEN lr.easy_required + lr.normal_required + p.success_count
          END AS value
        FROM cur c
        LEFT JOIN member_mission_progress p
          ON p.member_id = :memberId AND p.level = c.lvl
        JOIN level_requirement lr
          ON lr.level = c.lvl
""", nativeQuery = true)
    Integer getCurrentSuccessCount(@Param("memberId") Long memberId);

    @Query(value = """
        WITH cur AS (
          SELECT COALESCE(
            (SELECT level FROM member_mission_progress
              WHERE member_id = :memberId AND completed = 0 ORDER BY level DESC LIMIT 1),
            (SELECT MAX(level) FROM member_mission_progress
              WHERE member_id = :memberId AND completed = 1),
            1
          ) AS lvl
        )
        SELECT (lr.easy_required + lr.normal_required + lr.hard_required) AS value
        FROM cur c
        JOIN level_requirement lr ON lr.level = c.lvl
""", nativeQuery = true)
    Integer getCurrentTotalCount(@Param("memberId") Long memberId);

    Optional<MemberMissionProgress> findFirstByMemberIdAndCompletedFalseOrderByLevelDesc(Long memberId);


}
