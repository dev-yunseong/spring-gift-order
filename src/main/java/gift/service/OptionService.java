package gift.service;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.entity.OptionEntity;
import gift.entity.ProductEntity;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<OptionResponseDto> getProductOptions(long productId, Pageable pageable) {
        return optionRepository.findAllByProductEntityId(productId, pageable)
                .map(OptionResponseDto::new)
                .toList();
    }

    public void saveOption(long productId, OptionRequestDto optionRequestDto) {
        ProductEntity productEntity = productRepository.findWithOptionsById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product Not Found"));

        if (productEntity.isPending()) {
            throw new IllegalArgumentException("Product is Not Approved");
        }

        List<OptionEntity> optionEntities = productEntity.getOptionEntities();

        boolean isEmpty = optionEntities.isEmpty();

        boolean isDuplicated = optionEntities
                .stream().anyMatch(
                        optionEntity -> optionEntity.getName().equals(optionRequestDto.name())
                );

        if (isDuplicated) {
            throw new IllegalArgumentException("Duplicated Option's name");
        }

        OptionEntity optionEntity = new OptionEntity(optionRequestDto.name(), optionRequestDto.quantity(), productEntity);
        optionRepository.save(optionEntity);

        if (isEmpty) {
            productEntity.updateStatus(Product.Status.READY);
        }
    }

    public void subtractOptionQuantity(long optionId, int quantity) {
        OptionEntity optionEntity = optionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Option Not Found"));

        Option option = optionEntity.toDomain();

        int resultQuantity = option.subtractQuantity(quantity);

        if (resultQuantity == 0) {
            optionRepository.deleteById(optionId);
            ProductEntity productEntity = optionEntity.getProductEntity();
            productEntity.updateStatus(Product.Status.APPROVED);
        }
    }
}
