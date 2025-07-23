package gift.service;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.ProductStatusPatchRequestDto;
import gift.domain.Product;
import gift.entity.ProductEntity;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long saveProduct(ProductRequestDto productRequestDto) {
        Product product = productRequestDto.toDomain();
        ProductEntity productEntity = new ProductEntity(product);
        ProductEntity savedProductEntity = productRepository.save(productEntity);

        return savedProductEntity.getId();
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public void updateProduct(Long id, ProductRequestDto productRequestDto) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product Not Found"));

        Product product = productRequestDto.toDomain();

        productEntity.updateProduct(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findReadyProducts(Pageable pageable) {
        return productRepository.findAllByStatus(Product.Status.READY, pageable).stream()
                .map(ProductResponseDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponseDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponseDto findProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductResponseDto::new)
                .orElseThrow(() -> new IllegalArgumentException("Product Not Found"));
    }

    public void updateProductStatus(Long productId, ProductStatusPatchRequestDto statusPatchRequestDto) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product Not Found"));

        productEntity.updateStatus(statusPatchRequestDto.status());
    }

    public void updateStatus() {

    }
}
