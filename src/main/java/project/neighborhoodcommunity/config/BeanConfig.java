package project.neighborhoodcommunity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import project.neighborhoodcommunity.service.KakaoAccessTokenProviderService;
import project.neighborhoodcommunity.service.KakaoUserInfoProviderService;

@Configuration
public class BeanConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public KakaoAccessTokenProviderService kakaoAccessTokenService(RestTemplate restTemplate) {
        return new KakaoAccessTokenProviderService(restTemplate);
    }

    @Bean
    public KakaoUserInfoProviderService kakaoUserInfoProviderService(RestTemplate restTemplate) {
        return new KakaoUserInfoProviderService(restTemplate);
    }
}
