package project.neighborhoodcommunity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.neighborhoodcommunity.entity.User;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private final UserService userService;

    public User attemptLogin(String kakaoId) {
        return userService.findBykakaoId(kakaoId);
    }
}
