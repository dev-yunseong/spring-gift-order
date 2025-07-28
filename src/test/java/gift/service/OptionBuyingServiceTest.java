package gift.service;

import gift.client.KakaoApiClient;
import gift.domain.Product;
import gift.domain.member.SocialMember;
import gift.dto.OptionBuyingRequestDto;
import gift.entity.OptionBuyingEntity;
import gift.entity.OptionEntity;
import gift.entity.ProductEntity;
import gift.entity.member.SocialMemberEntity;
import gift.repository.OptionBuyingRepository;
import gift.repository.OptionRepository;
import gift.repository.member.CommonMemberRepository;
import gift.repository.member.SocialMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class OptionBuyingServiceTest {
    private OptionBuyingService optionBuyingService;

    @Mock
    private OptionBuyingRepository optionBuyingRepository;
    @Mock
    private OptionRepository optionRepository;
    @Mock
    private OptionService optionService;
    @Mock
    private WishService wishService;
    @Mock
    private KakaoApiClient kakaoApiClient;
    @Mock
    private SocialMemberRepository socialMemberRepository;
    @Mock
    private CommonMemberRepository commonMemberRepository;

    @BeforeEach
    void setup() {
        optionBuyingService = new OptionBuyingService(optionBuyingRepository, optionRepository, optionService, socialMemberRepository, commonMemberRepository, wishService, kakaoApiClient);
    }
    @Test
    void 구매시_WISH_감소_테스트() {
        ProductEntity productEntity = new ProductEntity(1L, "product1", 123, "path", Product.Status.READY);
        OptionEntity optionEntity = new OptionEntity(1L, "option1", 123, productEntity);
        OptionBuyingEntity optionBuyingEntity = new OptionBuyingEntity(1L, optionEntity, 123, "buying");
        optionBuyingEntity.prePersist();
        given(optionRepository.findById(any()))
                .willReturn(Optional.of(optionEntity));
        given(wishService.getWishCount(any(), any())).willReturn(10);
        given(optionBuyingRepository.save(any()))
                .willReturn(optionBuyingEntity);
        SocialMemberEntity memberEntity = new SocialMemberEntity(4L, 2L, SocialMember.Provider.KAKAO, "adsf", "asdf", "asdf", "adsf");
        given(commonMemberRepository.findById(eq(memberEntity.getId()))).willReturn(Optional.of(memberEntity));
        given(socialMemberRepository.findById(eq(memberEntity.getId()))).willReturn(Optional.of(memberEntity));

        optionBuyingService.buyOption(
                4L,
                new OptionBuyingRequestDto(1L, 12, "buying")
        );

        then(wishService).should().updateWishCount(eq(4L), eq(1L), eq(0));
    }

    @Test
    void 구매시_WISH_무시_테스트() {
        ProductEntity productEntity = new ProductEntity(1L, "product1", 123, "path", Product.Status.READY);
        OptionEntity optionEntity = new OptionEntity(1L, "option1", 123, productEntity);
        OptionBuyingEntity optionBuyingEntity = new OptionBuyingEntity(1L, optionEntity, 123, "buying");
        optionBuyingEntity.prePersist();
        OptionBuyingRequestDto requestDto = new OptionBuyingRequestDto(1L, 10, "buying");
        given(optionRepository.findById(any()))
                .willReturn(Optional.of(optionEntity));
        given(wishService.getWishCount(any(), any())).willReturn(0);
        given(optionBuyingRepository.save(any()))
                .willReturn(optionBuyingEntity);
        SocialMemberEntity memberEntity = new SocialMemberEntity(4L, 2L, SocialMember.Provider.KAKAO, "adsf", "asdf", "asdf", "adsf");
        given(commonMemberRepository.findById(eq(memberEntity.getId()))).willReturn(Optional.of(memberEntity));
        given(socialMemberRepository.findById(eq(memberEntity.getId()))).willReturn(Optional.of(memberEntity));

        optionBuyingService.buyOption(
                memberEntity.getId(),
                requestDto
        );

        then(wishService).should(never()).updateWishCount(anyLong(), anyLong(), anyInt());
    }

    @Test
    void 구매_테스트() {
        ProductEntity productEntity = new ProductEntity(1L, "product1", 123, "path", Product.Status.READY);
        OptionEntity optionEntity = new OptionEntity(1L, "옵션", 20, productEntity);
        given(optionRepository.findById(1L)).willReturn(Optional.of(optionEntity));
        given(wishService.getWishCount(any(), any())).willReturn(3);
        OptionBuyingEntity optionBuyingEntity = new OptionBuyingEntity(1L, optionEntity, 123, "buying");
        optionBuyingEntity.prePersist();
        given(optionBuyingRepository.save(any())).willReturn(optionBuyingEntity);
        OptionBuyingRequestDto request = new OptionBuyingRequestDto(1L, 5, "note");
        SocialMemberEntity memberEntity = new SocialMemberEntity(4L, 2L, SocialMember.Provider.KAKAO, "adsf", "asdf", "asdf", "adsf");
        given(commonMemberRepository.findById(eq(memberEntity.getId()))).willReturn(Optional.of(memberEntity));
        given(socialMemberRepository.findById(eq(memberEntity.getId()))).willReturn(Optional.of(memberEntity));

        optionBuyingService.buyOption(memberEntity.getId(), request);

        then(optionService).should().subtractOptionQuantity(1L, 5);
        then(optionBuyingRepository).should().save(any());
        then(wishService).should().updateWishCount(memberEntity.getId(), productEntity.getId(), 0);
    }

    @Test
    void Option_없을_시_구매_테스트() {
        given(optionRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> optionBuyingService.buyOption(
                1L,
                new OptionBuyingRequestDto(99L, 1, "fail")
        ));
    }

    @Test
    void Option_수량_부족시_구매_테스트() {
        ProductEntity productEntity = new ProductEntity(1L, "product1", 123, "path", Product.Status.READY);
        OptionEntity option = new OptionEntity(1L, "부족 옵션", 3, productEntity);
        given(optionRepository.findById(1L)).willReturn(Optional.of(option));

        OptionBuyingRequestDto request = new OptionBuyingRequestDto(1L, 10, "fail");

        assertThrows(IllegalArgumentException.class,
                () -> optionBuyingService.buyOption(1L, request));
    }
}
