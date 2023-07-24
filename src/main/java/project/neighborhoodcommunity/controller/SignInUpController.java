package project.neighborhoodcommunity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import project.constant.CommonResponseStatus;
import project.neighborhoodcommunity.dto.TokenDto;
import project.neighborhoodcommunity.dto.RequestSignUpDto;
import project.neighborhoodcommunity.dto.UserDto;
import project.neighborhoodcommunity.entity.User;
import project.neighborhoodcommunity.service.KakaoAccessTokenProviderService;
import project.neighborhoodcommunity.service.KakaoLoginService;
import project.neighborhoodcommunity.service.KakaoUserInfoProviderService;
import project.constant.CommonResponse;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class SignInUpController {

    private final KakaoAccessTokenProviderService kakaoAccessTokenProviderService;
    private final KakaoUserInfoProviderService kakaoUserInfoProviderService;
    private final KakaoLoginService kakaoLoginService;

    //https://kauth.kakao.com/oauth/authorize?client_id=26e821a2d76d661a073c984780a249f1&redirect_uri=http://localhost:8080&response_type=code
    @GetMapping("/kakao")
    @ResponseBody
    public ResponseEntity<?> kakaoLogin(String code) {
        String accessToken = kakaoAccessTokenProviderService.getAccessToken(code);
        RequestSignUpDto requestSignUpDto = kakaoUserInfoProviderService.getUserInfo(accessToken);
        Optional<User> user = kakaoLoginService.login(requestSignUpDto.getEmail());

        if (user.isPresent()) {
            TokenDto tokenDto = kakaoLoginService.loginSuccessToken(user.get());
            return new ResponseEntity<>(
                    new CommonResponse<>(tokenDto, CommonResponseStatus.SUCCESS),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(
                new CommonResponse<>(requestSignUpDto, CommonResponseStatus.LOGINFAILE),
                HttpStatus.UNAUTHORIZED);
    }
}
