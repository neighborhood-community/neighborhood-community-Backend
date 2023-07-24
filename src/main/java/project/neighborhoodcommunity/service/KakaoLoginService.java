package project.neighborhoodcommunity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.neighborhoodcommunity.dto.TokenDto;
import project.neighborhoodcommunity.entity.User;
import project.neighborhoodcommunity.jwt.JwtTokenProvider;
import project.neighborhoodcommunity.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public boolean attemptLogin(String kakaoId) {
        return userRepository.existsByKakaoid(kakaoId);
    }

    public TokenDto createToken(String kakaoId) {
        String accessToken = jwtTokenProvider.createToken(kakaoId);
        String refreshToken = jwtTokenProvider.createRefreshToken(kakaoId);
        saveRefreshToken(kakaoId, refreshToken);
        return new TokenDto(accessToken, refreshToken);
    }

    private void saveRefreshToken(String kakaoId, String refreshToken) {
        User user = new User();
        user.setKakaoid(kakaoId);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }
}
