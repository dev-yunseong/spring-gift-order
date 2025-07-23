package gift.repository;

import gift.domain.Product;
import gift.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void saveTest() {
        ProductEntity productEntity = new ProductEntity(null,"name", 1, "/path/", Product.Status.PENDING);
        ProductEntity actualProductEntity = productRepository.save(productEntity);

        assertAll(
                () -> assertThat(actualProductEntity.toDomain().getId()).isNotNull(),
                () -> assertThat(actualProductEntity.toDomain().getName()).isEqualTo(productEntity.toDomain().getName()),
                () -> assertThat(actualProductEntity.toDomain().getPrice()).isEqualTo(productEntity.toDomain().getPrice()),
                () -> assertThat(actualProductEntity.toDomain().getImageUrl()).isEqualTo(productEntity.toDomain().getImageUrl()),
                () -> assertThat(actualProductEntity.toDomain().getStatus()).isEqualTo(productEntity.toDomain().getStatus())
        );
    }

    @Test
    void findByEmailTest() {
        ProductEntity productEntity = new ProductEntity(null,"name", 1, "/path/", Product.Status.PENDING);
        productRepository.save(productEntity);

        Optional<ProductEntity> optionalMemberEntity = productRepository.findById(productEntity.getId());

        assertAll(
                () -> assertThat(optionalMemberEntity.isPresent()).isTrue(),
                () -> assertThat(optionalMemberEntity.get().toDomain().getId()).isNotNull(),
                () -> assertThat(optionalMemberEntity.get().toDomain().getName()).isEqualTo(productEntity.toDomain().getName()),
                () -> assertThat(optionalMemberEntity.get().toDomain().getPrice()).isEqualTo(productEntity.toDomain().getPrice()),
                () -> assertThat(optionalMemberEntity.get().toDomain().getImageUrl()).isEqualTo(productEntity.toDomain().getImageUrl()),
                () -> assertThat(optionalMemberEntity.get().toDomain().getStatus()).isEqualTo(productEntity.toDomain().getStatus())
        );
    }

    @Test
    void deleteTest() {
        ProductEntity productEntity = new ProductEntity(null,"name", 1, "/path/", Product.Status.PENDING);
        ProductEntity savedProductEntity = productRepository.save(productEntity);

        productRepository.deleteById(savedProductEntity.getId());

        Optional<ProductEntity> optionalProductEntity = productRepository.findById(savedProductEntity.getId());
        assertAll(
                () -> assertThat(optionalProductEntity.isEmpty()).isTrue()
        );
    }
}
