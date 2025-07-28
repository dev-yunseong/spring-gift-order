package gift.dto;

import gift.entity.OptionBuyingEntity;

import java.time.LocalDateTime;

public record OptionBuyingResponseDto(
	Long id,
	Long optionId,
	Integer quantity,
	LocalDateTime orderDateTime,
	String message
) {
	public OptionBuyingResponseDto(OptionBuyingEntity optionBuyingEntity) {
		this(
				optionBuyingEntity.getId(),
				optionBuyingEntity.getOptionEntity().getId(),
				optionBuyingEntity.getQuantity(),
				optionBuyingEntity.getOrderDateTime(),
				optionBuyingEntity.getMessage()
		);
	}
}