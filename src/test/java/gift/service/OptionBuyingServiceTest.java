package gift.service;

import gift.dto.OptionBuyingRequestDto;
import gift.entity.OptionBuyingEntity;
import gift.entity.OptionEntity;
import gift.repository.OptionBuyingRepository;
import gift.repository.OptionRepository;
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

    @BeforeEach
    void setup() {
        optionBuyingService = new OptionBuyingService(optionBuyingRepository, optionRepository, optionService, wishService);
    }
    @Test
    void 구매시_WISH_감소_테스트() {
        OptionEntity optionEntity = new OptionEntity(1L, "option1", 123, null);
        OptionBuyingEntity optionBuyingEntity = new OptionBuyingEntity(1L, optionEntity, 123, "buying");
        given(optionRepository.findById(any()))
                .willReturn(Optional.of(optionEntity));
        given(wishService.getWishCount(any(), any())).willReturn(10);
        given(optionBuyingRepository.save(any()))
                .willReturn(optionBuyingEntity);

        optionBuyingService.buyOption(
                4L,
                new OptionBuyingRequestDto(1L, 12, "buying")
        );

        then(wishService).should().updateWishCount(eq(4L), eq(1L), eq(0));
    }

    @Test
    void 구매시_WISH_무시_테스트() {
        OptionEntity optionEntity = new OptionEntity(1L, "option1", 123, null);
        OptionBuyingEntity optionBuyingEntity = new OptionBuyingEntity(1L, optionEntity, 123, "buying");
        OptionBuyingRequestDto requestDto = new OptionBuyingRequestDto(1L, 10, "buying");
        given(optionRepository.findById(any()))
                .willReturn(Optional.of(optionEntity));
        given(wishService.getWishCount(any(), any())).willReturn(0);
        given(optionBuyingRepository.save(any()))
                .willReturn(optionBuyingEntity);

        optionBuyingService.buyOption(
                4L,
                requestDto
        );

        then(wishService).should(never()).updateWishCount(anyLong(), anyLong(), anyInt());
    }

    @Test
    void 구매_테스트() {
        OptionEntity optionEntity = new OptionEntity(1L, "옵션", 20, null);
        given(optionRepository.findById(1L)).willReturn(Optional.of(optionEntity));
        given(wishService.getWishCount(any(), any())).willReturn(3);
        given(optionBuyingRepository.save(any())).willAnswer(inv -> inv.getArgument(0));
        OptionBuyingRequestDto request = new OptionBuyingRequestDto(1L, 5, "note");

        optionBuyingService.buyOption(10L, request);

        then(optionService).should().subtractOptionQuantity(1L, 5);
        then(optionBuyingRepository).should().save(any());
        then(wishService).should().updateWishCount(10L, 1L, 0);
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
        OptionEntity option = new OptionEntity(1L, "부족 옵션", 3, null);
        given(optionRepository.findById(1L)).willReturn(Optional.of(option));

        OptionBuyingRequestDto request = new OptionBuyingRequestDto(1L, 10, "fail");

        assertThrows(IllegalArgumentException.class,
                () -> optionBuyingService.buyOption(1L, request));
    }
}
