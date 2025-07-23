package gift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record OptionRequestDto(
        @Length(max = 50)
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+&/ _-]+$")
        String name,
        @Min(1)
        @Max(99999999)
        int quantity
) {
}
