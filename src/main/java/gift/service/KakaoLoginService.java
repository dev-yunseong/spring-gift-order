package gift.service;

import gift.domain.member.KakaoMember;
import gift.dto.KakaoTokenResponseDto;
import gift.dto.KakaoUserInfoResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;


@Service
public class KakaoLoginService {

    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_AUTHORIZE_URL = "https://kauth.kakao.com/oauth/authorize";
    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    private static final String TOKEN_PREFIX = "Bearer ";

    private final RestClient restClient;
    private final String kakaoAuthorizeUrl;
    private final String clientId;
    private final String redirectUrl;

    public KakaoLoginService(RestClient restClient, @Value("${kakao.rest-api-key}") String clientId, @Value("${kakao.redirect-url}") String redirectUrl) {
        this.restClient = restClient;
        this.clientId = clientId;
        this.redirectUrl = redirectUrl;
        kakaoAuthorizeUrl = KAKAO_AUTHORIZE_URL + "?scope=profile_nickname,profile_image,talk_message&response_type=code&redirect_uri=" + redirectUrl + "&client_id=" + clientId;
    }

    public String getKakaoAuthorizeUrl() {
        return kakaoAuthorizeUrl;
    }

    public String getAccessToken(String authorizeCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_url", redirectUrl);
        body.add("code", authorizeCode);

        RestClient.ResponseSpec responseSpec = restClient.post()
                .uri(KAKAO_TOKEN_URL)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(body)
                .retrieve();

        ResponseEntity<KakaoTokenResponseDto> responseEntity = responseSpec.toEntity(KakaoTokenResponseDto.class);

        if (responseEntity.getStatusCode().isError()) {
            throw new IllegalStateException("Fail Kakao Login");
        }

        KakaoTokenResponseDto kakaoTokenResponseDto = responseEntity.getBody();

        if (kakaoTokenResponseDto == null) {
            throw new IllegalStateException("Fail Kakao Login");
        }

        return kakaoTokenResponseDto.accessToken();
    }

    public KakaoMember getKakaoMember(String accessToken) {
        RestClient.ResponseSpec responseSpec = restClient.post()
                .uri(KAKAO_USER_INFO_URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + accessToken)
                .retrieve();

        ResponseEntity<KakaoUserInfoResponseDto> responseEntity = responseSpec.toEntity(KakaoUserInfoResponseDto.class);

        if (responseEntity.getStatusCode().isError()) {
            throw new IllegalStateException("Fail Kakao Login");
        }

        KakaoUserInfoResponseDto kakaoUserInfoResponseDto = responseEntity.getBody();

        if (kakaoUserInfoResponseDto == null) {
            throw new IllegalStateException("Fail Kakao Login");
        }

        return kakaoUserInfoResponseDto.toDomain();
    }
}