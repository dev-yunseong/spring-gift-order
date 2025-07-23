package gift.controller;

import gift.dto.AuthResponseDto;
import gift.dto.MemberRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {
    private final RestClient restClient = RestClient.builder().build();

    @LocalServerPort
    private int port;
    private String baseUrl;

    @BeforeEach
    void setBaseUrl() {
        baseUrl = "http://localhost:"+ port;
    }

    @Test
    void 회원가입_테스트() {
        ResponseEntity<AuthResponseDto> registerResponseEntity = registerMember("demo@demo", "pass");

        assertThat(registerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 로그인_성공_테스트() {
        registerMember("loginSuccess@demo", "pass");

        ResponseEntity<AuthResponseDto> loginResponseEntity = login("loginSuccess@demo", "pass");

        assertThat(loginResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 로그인_실패_테스트() {
        registerMember("loginFail@demo", "pass");

        HttpClientErrorException.BadRequest exception = Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> login("loginFail@demo", "nontpass"));

        assertThat(exception.getResponseBodyAsString()).contains("비밀번호");
    }

    @Test
    void 회원가입_중복_이메일_테스트() {
        ResponseEntity<AuthResponseDto> registerResponseEntity = registerMember("demo@email", "demopass");

        assertThat(registerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        HttpClientErrorException.BadRequest exception = Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> registerMember("demo@email", "demopass"));

        assertThat(exception.getResponseBodyAsString()).contains("email");
    }

    private ResponseEntity<AuthResponseDto> registerMember(String email, String password) {
        return restClient.post()
                .uri(baseUrl + "/api/members/register")
                .body(new MemberRequestDto(
                        email,
                        password
                ))
                .retrieve()
                .toEntity(AuthResponseDto.class);
    }

    private ResponseEntity<AuthResponseDto> login(String email, String password) {
        return restClient.post()
                .uri(baseUrl + "/api/members/login")
                .body(new MemberRequestDto(
                        email,
                        password
                ))
                .retrieve()
                .toEntity(AuthResponseDto.class);
    }
}
