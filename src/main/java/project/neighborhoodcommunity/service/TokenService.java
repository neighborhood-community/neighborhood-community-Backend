package project.neighborhoodcommunity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.neighborhoodcommunity.dto.TokenDto;
import project.neighborhoodcommunity.entity.User;
import project.neighborhoodcommunity.jwt.JwtTokenProvider;
import project.neighborhoodcommunity.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public TokenDto createToken(User user) {
        String accessToken = jwtTokenProvider.createToken(user.getKakaoid());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getKakaoid());
        saveRefreshToken(user, refreshToken);
        return new TokenDto(accessToken, refreshToken);
    }

    private void saveRefreshToken(User user, String refreshToken) {
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }
}
