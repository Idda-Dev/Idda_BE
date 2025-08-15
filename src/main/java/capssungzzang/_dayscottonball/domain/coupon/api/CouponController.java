package capssungzzang._dayscottonball.domain.coupon.api;

import capssungzzang._dayscottonball.domain.coupon.application.CouponService;
import capssungzzang._dayscottonball.domain.coupon.domain.entity.Coupon;
import capssungzzang._dayscottonball.domain.coupon.dto.CouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/users/{userId}/coupons/{couponId}")
    public ResponseEntity<Void> purchaseCoupon(@PathVariable("userId") Long memberId, @PathVariable("couponId") Long couponId) {
        Long myCouponId = couponService.purchase(memberId, couponId);
        URI location = URI.create("/api/users/" + memberId + "/my-coupons/" + myCouponId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<CouponResponse>> getAllCoupons() {
        List<CouponResponse> responses =  couponService.getAllCoupons();
        return ResponseEntity.ok(responses);
    }
}
