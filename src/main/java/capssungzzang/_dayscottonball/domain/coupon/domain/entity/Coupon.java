package capssungzzang._dayscottonball.domain.coupon.domain.entity;

import capssungzzang._dayscottonball.domain.store.domain.entity.Store;
import capssungzzang._dayscottonball.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String price;

    @Column
    private Integer maxCount;

    @Column(nullable = false)
    private int issuedCount;

    @Builder
    public Coupon(Store store, String title, String price, Integer maxCount, int issuedCount) {
        this.store = store;
        this.title = title;
        this.price = price;
        this.maxCount = maxCount;
        this.issuedCount = issuedCount;
    }
}
