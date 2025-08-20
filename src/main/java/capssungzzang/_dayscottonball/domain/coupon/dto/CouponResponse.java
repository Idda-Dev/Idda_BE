package capssungzzang._dayscottonball.domain.coupon.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponResponse {
    private Long couponId;
    private String storeName;
    private String title;
    private int price;
    private Integer maxCount;
    private int issuedCount;
}
