package gift.service;

import gift.dto.OptionBuyingRequestDto;
import gift.dto.OptionBuyingResponseDto;
import gift.entity.OptionBuyingEntity;
import gift.entity.OptionEntity;
import gift.repository.OptionBuyingRepository;
import gift.repository.OptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OptionBuyingService {
    private final OptionBuyingRepository optionBuyingRepository;
    private final OptionRepository optionRepository;
    private final OptionService optionService;
    private final WishService wishService;

    public OptionBuyingService(OptionBuyingRepository optionBuyingRepository, OptionRepository optionRepository, OptionService optionService, WishService wishService) {
        this.optionBuyingRepository = optionBuyingRepository;
        this.optionRepository = optionRepository;
        this.optionService = optionService;
        this.wishService = wishService;
    }

    public OptionBuyingResponseDto buyOption(long userId, OptionBuyingRequestDto optionBuyingRequestDto) {

        OptionEntity optionEntity = optionRepository.findById(optionBuyingRequestDto.optionId())
                .orElseThrow(() -> new IllegalArgumentException("Option Not Found"));

        if (optionEntity.getQuantity() < optionBuyingRequestDto.quantity()) {
            throw new IllegalArgumentException("Insufficient quantity available");
        }

        optionService.subtractOptionQuantity(
                optionBuyingRequestDto.optionId(),
                optionBuyingRequestDto.quantity()
        );

        OptionBuyingEntity optionBuyingEntity = new OptionBuyingEntity(optionBuyingRequestDto, optionEntity);

        OptionBuyingEntity savedOptionBuyingEntity = optionBuyingRepository.save(optionBuyingEntity);

        int wishCount = wishService.getWishCount(userId, optionEntity.getId());

        if (wishCount > 0) {
            int updatedWishCount = Math.max(wishCount - optionBuyingRequestDto.quantity(), 0);

            wishService.updateWishCount(userId, optionEntity.getId(), updatedWishCount);
        }

        return new OptionBuyingResponseDto(savedOptionBuyingEntity);
    }
}
