package gift.service;

import gift.domain.member.KakaoMember;
import gift.dto.AuthResponseDto;
import gift.dto.MemberRequestDto;
import gift.domain.member.Member;
import gift.security.JwtTokenProvider;
import gift.service.member.EmailMemberService;
import gift.service.member.KakaoMemberService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final EmailMemberService emailMemberService;
    private final KakaoMemberService kakaoMemberService;
    private final KakaoLoginService kakaoLoginService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(EmailMemberService emailMemberService, KakaoMemberService kakaoMemberService, KakaoLoginService kakaoLoginService, JwtTokenProvider jwtTokenProvider) {
        this.emailMemberService = emailMemberService;
        this.kakaoMemberService = kakaoMemberService;
        this.kakaoLoginService = kakaoLoginService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponseDto register(MemberRequestDto memberRequestDto) {
        Member member = emailMemberService.creatMember(memberRequestDto);
        String token = jwtTokenProvider.makeJwtToken(member.getId());
        return new AuthResponseDto(token);
    }

    public AuthResponseDto login(MemberRequestDto memberRequestDto) {
        Member member = emailMemberService.login(memberRequestDto);
        String token = jwtTokenProvider.makeJwtToken(member.getId());
        return new AuthResponseDto(token);
    }

    public AuthResponseDto registerOrLoginKakao(String authorizeCode) {
        String accessToken = kakaoLoginService.getAccessToken(authorizeCode);
        KakaoMember kakaoMember = kakaoLoginService.getKakaoMember(accessToken);
        Member member = kakaoMemberService.registerOrLogin(kakaoMember);
        String token = jwtTokenProvider.makeJwtToken(member.getId());
        return new AuthResponseDto(token);
    }
}
