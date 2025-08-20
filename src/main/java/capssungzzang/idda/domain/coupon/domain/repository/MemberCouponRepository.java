package capssungzzang.idda.domain.coupon.domain.repository;

import capssungzzang.idda.domain.coupon.domain.entity.MemberCoupon;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {

    Optional<MemberCoupon> findByIdAndMemberId(Long id, Long memberId);

    List<MemberCoupon> findAllByMemberId(Long memberId);

    @EntityGraph(attributePaths = {"coupon", "coupon.store"})
    List<MemberCoupon> findAllByMemberIdOrderByIdDesc(Long memberId);

//    @Modifying
//    @Query(value = """
//        UPDATE member_coupon
//        SET status = 'USED', used_at = NOW()
//        WHERE id = :memberCouponId
//          AND member_id = :memberId
//          AND status = 'CLAIMED'
//          AND expires_at >= NOW()
//        """, nativeQuery = true)
//    int tryRedeem(@Param("memberId") Long memberId,
//                  @Param("memberCouponId") Long memberCouponId);
}
