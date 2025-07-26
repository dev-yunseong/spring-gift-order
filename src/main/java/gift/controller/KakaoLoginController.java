package gift.controller;

import gift.dto.AuthResponseDto;
import gift.service.AuthService;
import gift.service.KakaoLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;
    private final AuthService authService;

    public KakaoLoginController(KakaoLoginService kakaoLoginService, AuthService authService) {
        this.kakaoLoginService = kakaoLoginService;
        this.authService = authService;
    }

    @GetMapping("/login/kakao")
    public String loginKakaoPage() {
        return "login";
    }

    @GetMapping("/oauth/kakao/authorize")
    public String authorizeKakao() {
        return "redirect:" + kakaoLoginService.getKakaoAuthorizeUrl();
    }

    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<AuthResponseDto> callback(@RequestParam("code") String authorizeCode) {
        AuthResponseDto authResponseDto = authService.registerOrLoginKakao(authorizeCode);
        return ResponseEntity.ok(authResponseDto);
    }
}
