package project.neighborhoodcommunity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import project.neighborhoodcommunity.dto.TokenDto;
import project.neighborhoodcommunity.dto.RequestSignUpDto;
import project.neighborhoodcommunity.entity.User;
import project.neighborhoodcommunity.service.*;
import project.constant.CommonResponse;

import java.util.Optional;

import static project.constant.CommonResponseStatus.*;

@Controller
@RequiredArgsConstructor
public class SignInUpController {

    private final KakaoAccessTokenProviderService kakaoAccessTokenProviderService;
    private final KakaoUserInfoProviderService kakaoUserInfoProviderService;
    private final KakaoLoginService kakaoLoginService;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    //https://kauth.kakao.com/oauth/authorize?client_id=26e821a2d76d661a073c984780a249f1&redirect_uri=http://localhost:5173/verify&response_type=code
    @GetMapping("/kakao")
    @ResponseBody
    public ResponseEntity<?> kakaoLogin(String code) {
        String accessToken = kakaoAccessTokenProviderService.getAccessToken(code);
        RequestSignUpDto requestSignUpDto = kakaoUserInfoProviderService.getUserInfo(accessToken);
        Optional<User> result = kakaoLoginService.attemptLogin(requestSignUpDto.getKakaoid());

        if (result.isEmpty())
            result = userService.join(requestSignUpDto);

        TokenDto tokenDto = jwtTokenService.createToken(result.get());
        return new ResponseEntity<>(new CommonResponse<>(tokenDto, SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/kakaotest")
    public String kakaotest() {
        return "redirect:https://kauth.kakao.com/oauth/authorize?client_id=26e821a2d76d661a073c984780a249f1&redirect_uri=http://localhost:5173/verify&response_type=code";
    }
}
