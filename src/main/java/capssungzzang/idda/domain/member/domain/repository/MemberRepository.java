package capssungzzang.idda.domain.member.domain.repository;

import capssungzzang.idda.domain.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Modifying
    @Query(value = """
        UPDATE member
        SET candy = candy - :price
        WHERE id = :memberId
          AND candy >= :price
        """, nativeQuery = true)
    int tryDeductCandy(@Param("memberId") Long memberId, @Param("price") int price);
}
