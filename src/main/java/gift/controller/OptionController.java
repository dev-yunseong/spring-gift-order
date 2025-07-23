package gift.controller;

import gift.dto.OptionRequestDto;
import gift.dto.OptionsResponseDto;
import gift.service.OptionService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/products")
@RestController
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<OptionsResponseDto> getOptions(@PathVariable long productId, Pageable pageable) {
        OptionsResponseDto optionsResponseDto = new OptionsResponseDto(
                optionService.getProductOptions(productId, pageable)
        );

        return ResponseEntity.ok(optionsResponseDto);
    }

    @PostMapping("/{productId}/options")
    public ResponseEntity<String> saveOption(
            @PathVariable long productId,
            @Validated @RequestBody OptionRequestDto optionRequestDto
    ) {
        optionService.saveOption(productId, optionRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Option is Successfully Added");
    }
}
