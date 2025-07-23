package gift.service;

import gift.domain.Product;
import gift.dto.OptionRequestDto;
import gift.entity.OptionEntity;
import gift.entity.ProductEntity;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class OptionServiceTest {
    @Mock
    private OptionRepository optionRepository;
    @Mock
    private ProductRepository productRepository;

    private OptionService optionService;

    @Captor
    ArgumentCaptor<OptionEntity> optionCaptor;

    @BeforeEach
    void initOptionService() {
        optionService = new OptionService(optionRepository, productRepository);
    }

    @Test
    void 옵션_추가_테스트() {
        ProductEntity productEntity = new ProductEntity(
                1L, "product 1", 123, "/path/to/image", Product.Status.APPROVED
        );
        given(productRepository.findWithOptionsById(any()))
                .willReturn(Optional.of(productEntity));

        optionService.saveOption(
                1,
                new OptionRequestDto(
                "option 1",
                1
        ));

        then(optionRepository).should().save(optionCaptor.capture());
        OptionEntity savedOption = optionCaptor.getValue();
        assertAll(
                () -> assertThat(savedOption.getId()).isNull(),
                () -> assertThat(savedOption.getName()).isEqualTo("option 1"),
                () -> assertThat(savedOption.getProductEntity().getStatus()).isEqualTo(Product.Status.READY)
        );
    }

    @Test
    void 옵션_수_줄이기_테스트() {
        ProductEntity productEntity = new ProductEntity(
                1L, "product 1", 123, "/path/to/image", Product.Status.READY
        );
        OptionEntity optionEntity = new OptionEntity(
                2L, "option2", 123, productEntity
        );
        productEntity.addOptionEntity(optionEntity);
        given(optionRepository.findById(any()))
                .willReturn(Optional.of(optionEntity));

        optionService.subtractOptionQuantity(
                2L,
                123);

        then(optionRepository).should().deleteById(eq(2L));
        assertThat(productEntity.getStatus()).isEqualTo(Product.Status.APPROVED);
    }
}
