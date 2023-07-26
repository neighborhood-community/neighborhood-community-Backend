package project.neighborhoodcommunity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import project.constant.CommonResponse;
import project.constant.CommonResponseStatus;
import project.neighborhoodcommunity.dto.TokenDto;
import project.neighborhoodcommunity.service.JwtTokenService;
import project.neighborhoodcommunity.service.UserService;

@Controller
@RequiredArgsConstructor
public class JwtController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @PostMapping("/newtoken")
    @ResponseBody
    public ResponseEntity<CommonResponse<TokenDto>> issueAccessToken(@RequestBody TokenDto refreshToken) {
        String kakaoId = userService.verifyToken(refreshToken.getRefreshToken());
        String token = jwtTokenService.createRefreshToken(kakaoId);
        return new ResponseEntity<>(new CommonResponse<>(new TokenDto(token), CommonResponseStatus.SUCCESS),HttpStatus.OK);
    }

    @GetMapping("/jwt")
    @ResponseBody
    public String TestJwt() {
        System.out.println(3);
        return "성공";
    }
}
