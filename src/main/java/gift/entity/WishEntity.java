package gift.entity;

import gift.entity.member.MemberEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "wishes")
public class WishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private int count;

    public void updateWishCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @ManyToOne
    @JoinColumn(name = "memberId")
    private MemberEntity memberEntity;

    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductEntity productEntity;

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    protected WishEntity() {}

    public WishEntity(int count, MemberEntity memberEntity, ProductEntity productEntity) {
        this.count = count;
        this.memberEntity = memberEntity;
        this.productEntity = productEntity;
    }
}
