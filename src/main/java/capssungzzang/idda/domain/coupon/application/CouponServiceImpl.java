package capssungzzang.idda.domain.coupon.application;

import capssungzzang.idda.domain.coupon.domain.entity.Coupon;
import capssungzzang.idda.domain.coupon.domain.entity.MemberCoupon;
import capssungzzang.idda.domain.coupon.domain.repository.CouponRepository;
import capssungzzang.idda.domain.coupon.domain.repository.MemberCouponRepository;
import capssungzzang.idda.domain.coupon.dto.CouponQRResponse;
import capssungzzang.idda.domain.coupon.dto.CouponResponse;
import capssungzzang.idda.domain.coupon.dto.MemberCouponResponse;
import capssungzzang.idda.domain.member.domain.entity.Member;
import capssungzzang.idda.domain.member.domain.repository.MemberRepository;
import capssungzzang.idda.global.s3.service.S3StorageService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final S3StorageService s3StorageService;

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
                    response.setStoreImageUrl(coupon.getStore().getStoreImageUrl());
                    response.setTitle(coupon.getTitle());
                    response.setPrice(coupon.getPrice());
                    response.setMaxCount(coupon.getMaxCount());
                    response.setIssuedCount(coupon.getIssuedCount());
                    return response;
                })
                .toList();
    }

    @Override
    public List<MemberCouponResponse> getAllMemberCoupons(Long memberId) {
        return memberCouponRepository.findAllByMemberIdOrderByIdDesc(memberId)
                .stream()
                .map(memberCoupon -> {
                    MemberCouponResponse response = new MemberCouponResponse();
                    response.setMemberCouponId(memberCoupon.getId());
                    response.setCouponId(memberCoupon.getCoupon().getId());
                    response.setStoreName(memberCoupon.getCoupon().getStore().getName());
                    response.setStoreImageUrl(memberCoupon.getCoupon().getStore().getStoreImageUrl());
                    response.setTitle(memberCoupon.getCoupon().getTitle());
                    response.setStatus(memberCoupon.getStatus().name());
                    response.setExpiresAt(memberCoupon.getExpiresAt());
                    return response;
                })
                .toList();
    }

    @Override
    public CouponQRResponse generateAndUploadQrUrl(Long userId, Long memberCouponId, boolean deterministicKey) {
        MemberCoupon memberCoupon = memberCouponRepository.findByIdAndMemberId(memberCouponId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."));

        byte[] png = createQrPng(memberCoupon.getCode(), 320);
        String url = s3StorageService.uploadQrPng(memberCouponId, png, deterministicKey);
        CouponQRResponse response = new CouponQRResponse();
        response.setUrl(url);
        return response;
    }

    private byte[] createQrPng(String content, int size) {
        try {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
            hints.put(EncodeHintType.MARGIN, 1);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

            BitMatrix matrix = new MultiFormatWriter()
                    .encode(content, BarcodeFormat.QR_CODE, size, size, hints);

            BufferedImage img = MatrixToImageWriter.toBufferedImage(matrix);

            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                ImageIO.write(img, "png", byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "QR 생성 실패", e);
        }
    }

}
