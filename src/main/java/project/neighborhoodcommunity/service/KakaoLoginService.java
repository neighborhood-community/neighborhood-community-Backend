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
public class KakaoLoginService {

    private final UserRepository userRepository;

    public Optional<User> attemptLogin(String kakaoId) {
        return userRepository.findByKakaoid(kakaoId);
    }


}
