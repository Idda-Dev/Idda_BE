package capssungzzang._dayscottonball.domain.coupon.domain.entity;

import capssungzzang._dayscottonball.domain.member.domain.entity.Member;
import capssungzzang._dayscottonball.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="coupon_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Coupon coupon;

    @Column(length=64, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status;

    @Column
    private LocalDateTime expiresAt;

//    @Column
//    private LocalDateTime usedAt;


    public enum Status { CLAIMED, USED, EXPIRED }

    @Builder
    public MemberCoupon(Member member, Coupon coupon, String code,
                        Status status, LocalDateTime expiresAt) {
        this.member = member;
        this.coupon = coupon;
        this.code = code;
        this.status = status;
        this.expiresAt = expiresAt;
    }

    public static MemberCoupon claimed(Member member, Coupon coupon, String code) {
        return MemberCoupon.builder()
                .member(member)
                .coupon(coupon)
                .code(code)
                .status(Status.CLAIMED)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();
    }
}
