package gift.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    private static final int CONNECT_TIMEOUT_SECONDS = 3;
    private static final int READE_TIMEOUT_SECONDS = 3;

    @Bean
    public RestClient restClient() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(CONNECT_TIMEOUT_SECONDS));
        requestFactory.setReadTimeout(Duration.ofSeconds(READE_TIMEOUT_SECONDS));

        return RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }
}
