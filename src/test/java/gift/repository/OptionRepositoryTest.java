package gift.repository;

import gift.domain.Product;
import gift.entity.OptionEntity;
import gift.entity.ProductEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void 옵션_추가_테스트() {
        ProductEntity productEntity = productRepository.save(new ProductEntity(null, "product1", 123, "path", Product.Status.APPROVED));
        OptionEntity optionEntity = optionRepository.save(new OptionEntity("name1", 123, productEntity));

        assertThat(optionEntity.getId()).isNotNull();
    }

    @Test
    void 옵션_이름_중복_테스트() {
        ProductEntity productEntity = productRepository.save(new ProductEntity(null, "product1", 123, "path", Product.Status.APPROVED));
        optionRepository.save(new OptionEntity("name1", 123, productEntity));

        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> optionRepository.save(new OptionEntity("name1", 321, productEntity))
        );
    }

    @Test
    void 옵션_확인_테스트() {
        ProductEntity productEntity = productRepository.save(new ProductEntity(null, "product1", 123, "path", Product.Status.APPROVED));
        optionRepository.save(new OptionEntity("name1", 123, productEntity));
        entityManager.flush();
        entityManager.clear();
        ProductEntity savedProductEntity = productRepository.findById(productEntity.getId()).get();

        assertThat(savedProductEntity.getOptionEntities().size()).isEqualTo(1);
    }

    @Test
    void 옵션_유무_필터링_테스트() {
        int originalNum = productRepository.findAllByStatusHavingOptions(Product.Status.APPROVED, Pageable.unpaged()).getSize();
        productRepository.save(new ProductEntity(null, "product1", 123, "path", Product.Status.APPROVED));
        int nextNum = productRepository.findAllByStatusHavingOptions(Product.Status.APPROVED, Pageable.unpaged()).getSize();

        assertThat(originalNum).isEqualTo(nextNum);
    }
}
