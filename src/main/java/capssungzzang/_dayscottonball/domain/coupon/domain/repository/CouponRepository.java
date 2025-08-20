package capssungzzang._dayscottonball.domain.coupon.domain.repository;

import capssungzzang._dayscottonball.domain.coupon.domain.entity.Coupon;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Modifying
    @Query(value = """
        UPDATE coupon
        SET issued_count = issued_count + 1
        WHERE id = :couponId
          AND (max_count IS NULL OR issued_count < max_count)
        """, nativeQuery = true)
    int tryIncreaseIssued(@Param("couponId") Long couponId);

    @EntityGraph(attributePaths = "store")
    @Query("select c from Coupon c order by c.id desc")
    List<Coupon> findAllWithStore();
}
