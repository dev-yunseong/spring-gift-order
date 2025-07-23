package gift.entity;

import gift.domain.Option;
import gift.domain.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "options")
public class OptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    @Column(nullable = false, length = 50)
    private String name;

    public String getName() {
        return name;
    }

    @Column(nullable = false)
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductEntity productEntity;

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public Option toDomain() {
        return new Option(
                id,
                name,
                quantity,
                new Product(
                        productEntity.getName(),
                        productEntity.getPrice(),
                        productEntity.getImageUrl())

        );
    }

    protected OptionEntity() {}

    public OptionEntity(String name, int quantity, ProductEntity productEntity) {
        this(null, name, quantity, productEntity);
    }

    public OptionEntity(Option option) {
        this(
                option.getId(),
                option.getName(),
                option.getQuantity(),
                new ProductEntity(option.getProduct())
        );
    }

    public OptionEntity(Long id, String name, int quantity, ProductEntity productEntity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.productEntity = productEntity;
    }
}
