package gift.dto;

import gift.domain.Product;
import gift.entity.ProductEntity;

public record ProductResponseDto(Long id, String name, Integer price, String imageUrl, Product.Status status) {
    public ProductResponseDto(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getStatus());
    }

    public ProductResponseDto(ProductEntity productEntity) {
        this(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl(), productEntity.getStatus());
    }
}
