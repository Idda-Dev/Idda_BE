package capssungzzang._dayscottonball.domain.coupon.application;

import capssungzzang._dayscottonball.domain.coupon.dto.CouponQRResponse;
import capssungzzang._dayscottonball.domain.coupon.dto.CouponResponse;
import capssungzzang._dayscottonball.domain.coupon.dto.MemberCouponResponse;

import java.util.List;

public interface CouponService {
    Long purchase(Long memberId, Long couponId);
    List<CouponResponse> getAllCoupons();
    List<MemberCouponResponse> getAllMemberCoupons(Long memberId);
    CouponQRResponse generateAndUploadQrUrl(Long userId, Long memberCouponId, boolean deterministicKey);
}
