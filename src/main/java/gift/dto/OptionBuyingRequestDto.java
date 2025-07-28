package gift.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OptionBuyingRequestDto(
	@NotNull
	Long optionId,
	@NotNull
	Integer quantity,
	@NotBlank
	String message
) {}