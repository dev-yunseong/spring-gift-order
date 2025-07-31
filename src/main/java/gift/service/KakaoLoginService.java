package gift.service;

import gift.client.KakaoApiClient;
import gift.domain.member.SocialMember;
import gift.dto.KakaoTokenResponseDto;
import gift.dto.KakaoUserInfoResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class KakaoLoginService {

    private static final String KAKAO_AUTHORIZE_URL = "https://kauth.kakao.com/oauth/authorize";

    private final String kakaoAuthorizeUrl;
    private final KakaoApiClient kakaoApiClient;

    public KakaoLoginService( @Value("${kakao.rest-api-key}") String clientId, @Value("${kakao.redirect-url}") String redirectUrl, KakaoApiClient kakaoApiClient) {
        this.kakaoApiClient = kakaoApiClient;
        kakaoAuthorizeUrl = KAKAO_AUTHORIZE_URL + "?scope=profile_nickname,profile_image,talk_message&response_type=code&redirect_uri=" + redirectUrl + "&client_id=" + clientId;
    }

    public String getKakaoAuthorizeUrl() {
        return kakaoAuthorizeUrl;
    }

    public KakaoTokenResponseDto getTokens(String authorizeCode) {
        KakaoTokenResponseDto kakaoTokenResponseDto = kakaoApiClient.requestAccessToken(authorizeCode);

        if (kakaoTokenResponseDto == null) {
            throw new IllegalStateException("Fail Kakao Login");
        }

        return kakaoTokenResponseDto;
    }

    public SocialMember getSocialMember(String accessToken, String refreshToken) {
       KakaoUserInfoResponseDto kakaoUserInfoResponseDto = kakaoApiClient.requestSocialMember(accessToken);

        if (kakaoUserInfoResponseDto == null) {
            throw new IllegalStateException("Fail Kakao Login");
        }

        return kakaoUserInfoResponseDto.toDomain(
                accessToken,
                refreshToken
        );
    }
}