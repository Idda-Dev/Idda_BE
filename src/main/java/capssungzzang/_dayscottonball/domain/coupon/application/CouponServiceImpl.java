package capssungzzang._dayscottonball.domain.coupon.application;

import capssungzzang._dayscottonball.domain.coupon.domain.entity.Coupon;
import capssungzzang._dayscottonball.domain.coupon.domain.entity.MemberCoupon;
import capssungzzang._dayscottonball.domain.coupon.domain.repository.CouponRepository;
import capssungzzang._dayscottonball.domain.coupon.domain.repository.MemberCouponRepository;
import capssungzzang._dayscottonball.domain.coupon.dto.CouponResponse;
import capssungzzang._dayscottonball.domain.member.domain.entity.Member;
import capssungzzang._dayscottonball.domain.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final MemberCouponRepository memberCouponRepository;

    @Transactional
    @Override
    public Long purchase(Long memberId, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "쿠폰이 존재하지 않습니다."));

        int price = coupon.getPrice();

        // 재고 확인
        int increasedCount = couponRepository.tryIncreaseIssued(couponId);
        if (increasedCount == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "쿠폰 수량이 소진되었습니다.");
        }

        // 캔디 차감
        int decreasedCount = memberRepository.tryDeductCandy(memberId, price);
        if (decreasedCount == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "캔디가 부족합니다.");
        }

        // 쿠폰 발급
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));

        MemberCoupon newCoupon = memberCouponRepository.save(
                MemberCoupon.claimed(member, coupon, UUID.randomUUID().toString())
        );

        return newCoupon.getId();
    }

    @Override
    public List<CouponResponse> getAllCoupons() {
        List<Coupon> coupons = couponRepository.findAllWithStore();
        return coupons.stream()
                .map(coupon -> {
                    CouponResponse response = new CouponResponse();
                    response.setCouponId(coupon.getId());
                    response.setStoreName(coupon.getStore().getName());
                    response.setTitle(coupon.getTitle());
                    response.setPrice(coupon.getPrice());
                    response.setMaxCount(coupon.getMaxCount());
                    response.setIssuedCount(coupon.getIssuedCount());
                    return response;
                })
                .toList();
    }


}
