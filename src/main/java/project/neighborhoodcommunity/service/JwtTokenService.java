package project.neighborhoodcommunity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.neighborhoodcommunity.dto.TokenDto;
import project.neighborhoodcommunity.entity.User;
import project.neighborhoodcommunity.jwt.JwtTokenProvider;
import project.neighborhoodcommunity.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public TokenDto generateTokenForUser(User user, String profileImg) {
        TokenDto tokens = createTokensForUser(user.getKakaoid());
        updateUserWithTokensAndProfileImage(user, tokens.getRefreshToken(), profileImg);
        return tokens;
    }

    private void updateUserWithTokensAndProfileImage(User user, String refreshToken, String profileImg) {
        user.setRefreshToken(refreshToken);
        user.setProfile_img(profileImg);
        userRepository.save(user);
    }

    private TokenDto createTokensForUser(String kakaoId) {
        String accessToken = jwtTokenProvider.createToken(kakaoId);
        String refreshToken = jwtTokenProvider.createRefreshToken(kakaoId);
        return new TokenDto(accessToken, refreshToken);
    }

    public String createRefreshToken(String kakaoId) {
        return jwtTokenProvider.createToken(kakaoId);
    }
}
