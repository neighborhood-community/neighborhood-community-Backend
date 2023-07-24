package project.neighborhoodcommunity.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoAccessTokenProviderService {

    private final RestTemplate restTemplate;

    public String getAccessToken(String code) {
        JsonNode root = sendAccessTokenRequest(code).getBody();
        return root.get("access_token").asText();
    }

    private HttpHeaders createHeadersWithContentType() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

    private MultiValueMap<String, String> createAccessTokenParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "26e821a2d76d661a073c984780a249f1");
        params.add("redirect_url", "http://localhost:8080");
        params.add("code", code);
        return params;
    }

    private HttpEntity<MultiValueMap<String, String>> createAccessTokenRequest(String code) {
        HttpHeaders headers = createHeadersWithContentType();
        MultiValueMap<String, String> params = createAccessTokenParams(code);
        return new HttpEntity<>(params, headers);
    }

    private ResponseEntity<JsonNode> sendAccessTokenRequest(String code) {
        return restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                createAccessTokenRequest(code),
                JsonNode.class
        );
    }
}
