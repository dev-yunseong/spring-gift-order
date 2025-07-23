package gift.controller;

import gift.dto.OptionRequestDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.domain.Product;
import gift.dto.ProductsResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    private final RestClient client = RestClient.builder().build();

    @LocalServerPort
    private int port;
    private String baseUrl;

    @BeforeEach
    void setBaseUrl() {
        baseUrl = "http://localhost:"+ port;
    }

    @ParameterizedTest
    @MethodSource("invalidProducts")
    void Validation_테스트(ProductRequestDto productRequestDto, List<String> messages) {
        HttpClientErrorException.BadRequest exception = assertThrows(HttpClientErrorException.BadRequest.class,
                () -> {
                    addProduct(productRequestDto);
                });

        boolean anyMatch = messages.stream()
                .anyMatch(exception.getMessage()::contains);

        assertThat(anyMatch).isTrue();
    }

    Stream<Arguments> invalidProducts() {
        return Stream.of(
                Arguments.of(new ProductRequestDto(
                        "1234567890123456",
                        123,
                        "http://~/~"
                ), List.of("length", "길이")),
                Arguments.of(new ProductRequestDto(
                        "$#",
                        123,
                        "http://~/~"
                ), List.of("special")),
                Arguments.of(new ProductRequestDto(
                        "1234567890",
                        -1,
                        "http://~/~"
                ), List.of("price"))
        );
    }

    @Test
    void 카카오_들어가는_이름_테스트_입력_테스트() {
        ResponseEntity<String> response = addProduct(new ProductRequestDto(
                "카카오 들어감",
                123,
                "http://path/"
        ));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String responseMessage = response.getBody();
        assertThat(responseMessage).isNotNull();

        Pattern pattern = Pattern.compile("id: (\\d+)");
        Matcher matcher = pattern.matcher(responseMessage);
        Long id;

        assertThat(matcher.find()).isTrue();

        String idStr = matcher.group(1);
        id = Long.parseLong(idStr);

        ResponseEntity<ProductResponseDto> entity = getProduct(id);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProductResponseDto productResponseDto = entity.getBody();
        assertThat(productResponseDto.status()).isEqualTo(Product.Status.PENDING);
    }

    @Test
    void 존재하는_상품_읽기_테스트() {
        ResponseEntity<ProductResponseDto> entity = getProduct(1L);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 존재하지_않는_제폼_테스트() {
        HttpClientErrorException.BadRequest exception = assertThrows(HttpClientErrorException.BadRequest.class,
                () -> {
                    getProduct(-1L);
                });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void Pagenation_Test() {
        for (int i = 0; i < 10; i++) {
            addProduct(new ProductRequestDto("name" + i, 123, "path"));
            addOption(1+i, new OptionRequestDto("name" + i, 123));
        }

        ResponseEntity<ProductsResponseDto> responseEntity = getProducts(PageRequest.of(0, 5));

        assertAll(
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK),
                () ->assertThat(responseEntity.getBody().products().size()).isEqualTo(5)
        );
    }

    private ResponseEntity<ProductsResponseDto> getProducts(PageRequest pageRequest) {
        return client.get()
                .uri(baseUrl + "/api/products?"
                + "page=" + pageRequest.getPageNumber() + "&"
                + "size=" + pageRequest.getPageSize())
                .retrieve()
                .toEntity(ProductsResponseDto.class);
    }

    private ResponseEntity<String> addProduct(ProductRequestDto productRequestDto) {
        return client.post()
                .uri(baseUrl + "/api/products")
                .body(productRequestDto)
                .retrieve()
                .toEntity(String.class);
    }

    private ResponseEntity<String> addOption(long productId, OptionRequestDto optionRequestDto) {
        return client.post()
                .uri(baseUrl + "/api/products/" + productId + "/options")
                .body(optionRequestDto)
                .retrieve()
                .toEntity(String.class);
    }

    private ResponseEntity<ProductResponseDto> getProduct(long id) {
        return client.get()
                .uri(baseUrl + "/api/products/" + id)
                .retrieve()
                .toEntity(ProductResponseDto.class);
    }
}
