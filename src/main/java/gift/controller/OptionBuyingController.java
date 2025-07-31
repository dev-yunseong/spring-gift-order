package gift.controller;

import gift.config.LoginMember;
import gift.domain.member.Member;
import gift.dto.OptionBuyingRequestDto;
import gift.dto.OptionBuyingResponseDto;
import gift.service.OptionBuyingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/option-buyings")
@RestController
public class OptionBuyingController {

    private final OptionBuyingService optionBuyingService;

    public OptionBuyingController(OptionBuyingService optionBuyingService) {
        this.optionBuyingService = optionBuyingService;
    }

    @PostMapping("")
    public ResponseEntity<OptionBuyingResponseDto> buyOption(
            @LoginMember Member member,
            @Validated @RequestBody OptionBuyingRequestDto optionBuyingRequestDto) {

        OptionBuyingResponseDto optionBuyingResponseDto = optionBuyingService.buyOption(
                member.getId(),
                optionBuyingRequestDto
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(optionBuyingResponseDto);
    }


}
