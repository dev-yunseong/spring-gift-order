package gift.service;

import gift.domain.member.SocialMember;
import gift.dto.KakaoTokenResponseDto;
import gift.dto.KakaoUserInfoResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KakaoLoginServiceTest {

    @Mock
    private RestClient mockRestClient;
    @Mock
    private RestClient.RequestBodyUriSpec mockRequestBodyUriSpec;
    @Mock
    private RestClient.ResponseSpec mockResponseSpec;

    private KakaoLoginService kakaoLoginService;

    private final String clientId = "dummy-client-id";
    private final String redirectUrl = "http://localhost/oauth/kakao/callback";

    @BeforeEach
    void setUp() {
        given(mockRestClient.post()).willReturn(mockRequestBodyUriSpec);
        given(mockRequestBodyUriSpec.uri(anyString())).willReturn(mockRequestBodyUriSpec);
        given(mockRequestBodyUriSpec.header(anyString(), anyString())).willReturn(mockRequestBodyUriSpec);
        given(mockRequestBodyUriSpec.retrieve()).willReturn(mockResponseSpec);

        kakaoLoginService = new KakaoLoginService(mockRestClient, clientId, redirectUrl);
    }

    @Test
    void Access_Token_테스트() {
        given(mockRequestBodyUriSpec.body(any(MultiValueMap.class))).willReturn(mockRequestBodyUriSpec);
        KakaoTokenResponseDto fakeToken =
                new KakaoTokenResponseDto(
                        "Bearer",
                        "access-token",
                        3600,
                        "refresh-token",
                        1234);
        given(mockResponseSpec.toEntity(KakaoTokenResponseDto.class))
                .willReturn(new ResponseEntity<>(fakeToken, HttpStatus.OK));

        String token = kakaoLoginService.getAccessToken("authorize-code");

        assertThat(token).isEqualTo("access-token");
    }

    @Test
    void Access_Token_Null_처리_테스트() {
        given(mockRequestBodyUriSpec.body(any(MultiValueMap.class))).willReturn(mockRequestBodyUriSpec);
        when(mockResponseSpec.toEntity(KakaoTokenResponseDto.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        assertThrows(IllegalStateException.class,
                () -> kakaoLoginService.getAccessToken("auth-code"));
    }

    @Test
    void Kakao_Member_테스트() {
        KakaoUserInfoResponseDto dto = new KakaoUserInfoResponseDto(12345L, "nickname", "/path/to/image");
        SocialMember expected = new SocialMember(null, 12345L, SocialMember.Provider.KAKAO, "nickname", "/path/to/image");
        given(mockResponseSpec.toEntity(KakaoUserInfoResponseDto.class))
                .willReturn(new ResponseEntity<>(dto, HttpStatus.OK));

        SocialMember result = kakaoLoginService.getKakaoMember("access-token");

        assertThat(result.getProviderId()).isEqualTo(expected.getProviderId());
    }

    @Test
    void Kakao_Member_Null_처리_테스트() {
        given(mockResponseSpec.toEntity(KakaoUserInfoResponseDto.class))
                .willReturn(new ResponseEntity<>(null, HttpStatus.OK));

        assertThrows(IllegalStateException.class,
                () -> kakaoLoginService.getKakaoMember("access-token"));
    }
}

