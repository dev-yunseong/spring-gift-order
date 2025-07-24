package gift.service;

import gift.dto.ProductResponseDto;
import gift.dto.WishResponseDto;
import gift.entity.member.MemberEntity;
import gift.entity.ProductEntity;
import gift.entity.WishEntity;
import gift.repository.member.EmailMemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class WishService {

    public final WishRepository wishRepository;
    public final ProductRepository productRepository;
    public final EmailMemberRepository emailMemberRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository, EmailMemberRepository emailMemberRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.emailMemberRepository = emailMemberRepository;
    }

    public void saveWish(long memberId, long productId, int count) {
        if (count == 0){
            return;
        }

        if (wishRepository.findWishesByMemberEntityIdAndProductEntityId(memberId, productId).isPresent()) {
            throw new IllegalArgumentException("Wish is Already Existed");
        }

        MemberEntity memberEntity = emailMemberRepository.findById(memberId)
                        .orElseThrow(() -> new IllegalArgumentException("Member Not Found"));
        ProductEntity productEntity = productRepository.findById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Product Not Found"));
        WishEntity wishEntity = new WishEntity(count, memberEntity, productEntity);

        wishRepository.save(wishEntity);
    }

    public void updateWishCount(long memberId, long productId, int count) {
        WishEntity wishEntity = wishRepository.findWishesByMemberEntityIdAndProductEntityId(memberId, productId)
                .orElseThrow(() -> new IllegalArgumentException("Wish Not Found"));

        if (count == 0) {
            wishRepository.delete(wishEntity);
            return;
        }

        wishEntity.updateWishCount(count);
    }

    @Transactional(readOnly = true)
    public List<WishResponseDto> getWishList(long memberId, Pageable pageable) {
        return wishRepository.findByMemberEntityId(memberId, pageable)
                .map(
                    wishEntity ->
                            new WishResponseDto(
                                    wishEntity.getCount(),
                                    new ProductResponseDto(
                                            wishEntity.getProductEntity().toDomain()
                                    )
                            )
                )
                .toList();
    }
}
