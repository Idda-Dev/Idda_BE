package capssungzzang.idda.domain.store.domain.entity;

import capssungzzang.idda.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2048)
    private String storeImageUrl;

    @Builder
    public Store(String name,  String storeImageUrl) {
        this.name = name;
        this.storeImageUrl = storeImageUrl;
    }
}
