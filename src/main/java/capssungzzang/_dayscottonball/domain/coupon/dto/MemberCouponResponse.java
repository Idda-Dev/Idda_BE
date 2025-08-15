package capssungzzang._dayscottonball.domain.coupon.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberCouponResponse {
    private Long memberCouponId;
    private Long couponId;
    private String storeName;
    private String title;
    private String status;
    private LocalDateTime expiresAt;
}
