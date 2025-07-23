package gift.dto;

import jakarta.validation.constraints.*;

public record OptionRequestDto(
        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+&/ _-]+$")
        String name,
        @Min(1)
        @Max(99999999)
        int quantity
) {
}
