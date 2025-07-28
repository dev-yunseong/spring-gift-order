package gift.service;

import gift.client.KakaoApiClient;
import gift.domain.member.SocialMember;
import gift.dto.KakaoTokenResponseDto;
import gift.dto.KakaoUserInfoResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KakaoLoginServiceTest {

    @Mock
    private KakaoApiClient kakaoApiClient;

    private KakaoLoginService kakaoLoginService;

    private final String clientId = "dummy-client-id";
    private final String redirectUrl = "http://localhost/oauth/kakao/callback";

    @BeforeEach
    void setUp() {
        kakaoLoginService = new KakaoLoginService(clientId, redirectUrl, kakaoApiClient);
    }

    @Test
    void Access_Token_테스트() {
        KakaoTokenResponseDto fakeToken =
                new KakaoTokenResponseDto(
                        "Bearer",
                        "access-token",
                        3600,
                        "refresh-token",
                        1234);
        given(kakaoApiClient.requestAccessToken(any()))
                .willReturn(fakeToken);

        KakaoTokenResponseDto token = kakaoLoginService.getTokens("authorize-code");

        assertThat(token.accessToken()).isEqualTo("access-token");
    }

    @Test
    void Access_Token_Null_처리_테스트() {
        given(kakaoApiClient.requestAccessToken(any())).willReturn(null);

        assertThrows(IllegalStateException.class,
                () -> kakaoLoginService.getTokens("auth-code"));
    }

    @Test
    void Kakao_Member_테스트() {
        KakaoUserInfoResponseDto dto = new KakaoUserInfoResponseDto(12345L, "nickname", "/path/to/image");
        SocialMember expected = new SocialMember(null, 12345L, SocialMember.Provider.KAKAO, "nickname", "/path/to/image", "access-token", "refresh-token");
        given(kakaoApiClient.requestSocialMember(any()))
                .willReturn(dto);

        SocialMember result = kakaoLoginService.getSocialMember("access-token", "refresh-token");

        assertThat(result.getProviderId()).isEqualTo(expected.getProviderId());
    }

    @Test
    void Kakao_Member_Null_처리_테스트() {
        given(kakaoApiClient.requestSocialMember(any()))
                .willReturn(null);

        assertThrows(IllegalStateException.class,
                () -> kakaoLoginService.getSocialMember("access-token", "refresh-token"));
    }
}

