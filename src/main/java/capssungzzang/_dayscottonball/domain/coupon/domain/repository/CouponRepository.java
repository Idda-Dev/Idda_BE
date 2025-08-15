package capssungzzang._dayscottonball.domain.coupon.domain.repository;

import capssungzzang._dayscottonball.domain.coupon.domain.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Modifying
    @Query(value = """
        UPDATE coupon
        SET issued_count = issued_count + 1
        WHERE id = :couponId
          AND (max_count IS NULL OR issued_count < max_count)
        """, nativeQuery = true)
    int tryIncreaseIssued(@Param("couponId") Long couponId);
}
