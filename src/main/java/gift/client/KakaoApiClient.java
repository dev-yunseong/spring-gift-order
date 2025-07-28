package gift.client;

import gift.dto.KakaoTokenResponseDto;
import gift.dto.KakaoUserInfoResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoApiClient {

    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    private static final String TOKEN_PREFIX = "Bearer ";

    private final RestClient restClient;
    private final String clientId;
    private final String redirectUrl;

    public KakaoApiClient(RestClient restClient, @Value("${kakao.rest-api-key}") String clientId, @Value("${kakao.redirect-url}") String redirectUrl) {
        this.restClient = restClient;
        this.clientId = clientId;
        this.redirectUrl = redirectUrl;
    }

    public KakaoTokenResponseDto requestAccessToken(String authorizeCode) {
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

        return responseEntity.getBody();
    }

    public KakaoUserInfoResponseDto requestSocialMember(String accessToken) {
        RestClient.ResponseSpec responseSpec = restClient.post()
                .uri(KAKAO_USER_INFO_URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + accessToken)
                .retrieve();

        ResponseEntity<KakaoUserInfoResponseDto> responseEntity = responseSpec.toEntity(KakaoUserInfoResponseDto.class);

        if (responseEntity.getStatusCode().isError()) {
            throw new IllegalStateException("Fail Kakao Login");
        }

        return responseEntity.getBody();
    }
}