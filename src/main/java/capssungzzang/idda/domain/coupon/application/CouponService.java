package capssungzzang.idda.domain.coupon.application;

import capssungzzang.idda.domain.coupon.dto.CouponQRResponse;
import capssungzzang.idda.domain.coupon.dto.CouponResponse;
import capssungzzang.idda.domain.coupon.dto.MemberCouponResponse;

import java.util.List;

public interface CouponService {
    Long purchase(Long memberId, Long couponId);
    List<CouponResponse> getAllCoupons();
    List<MemberCouponResponse> getAllMemberCoupons(Long memberId);
    CouponQRResponse generateAndUploadQrUrl(Long userId, Long memberCouponId, boolean deterministicKey);
}
