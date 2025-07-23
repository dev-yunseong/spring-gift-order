package gift.entity;

import gift.domain.Product;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    @Column(nullable = false, length = 15)
    private String name;

    public String getName() {
        return name;
    }

    @Column(nullable = false)
    private int price;

    public int getPrice() {
        return price;
    }

    @Column(nullable = false, length = 255)
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Product.Status status;

    @OneToMany(mappedBy = "productEntity")
    private List<OptionEntity> optionEntities = new ArrayList<>();

    public List<OptionEntity> getOptionEntities() {
        return optionEntities;
    }

    public void addOptionEntity(OptionEntity optionEntity) {
        optionEntities.add(optionEntity);
    }

    public Product.Status getStatus() {
        return status;
    }

    public Product toDomain() {
        return new Product(id, name, price, imageUrl, status);
    }

    public boolean isApproved() {
        return status == Product.Status.APPROVED;
    }

    public boolean isPending() {
        return status == Product.Status.PENDING;
    }

    protected ProductEntity() {}

    public ProductEntity(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getStatus());
    }

    public ProductEntity(Long id, String name, int price, String imageUrl, Product.Status status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public void updateProduct(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.status = product.getStatus();
    }

    public void updateStatus(Product.Status status) {
        this.status = status;
    }
}
