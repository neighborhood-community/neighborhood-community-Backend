package project.neighborhoodcommunity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.constant.CommonResponse;
import project.constant.CommonResponseStatus;
import project.neighborhoodcommunity.dto.TokenDto;
import project.neighborhoodcommunity.jwt.JwtTokenProvider;
import project.neighborhoodcommunity.service.JwtTokenService;
import project.neighborhoodcommunity.service.UserService;

@Controller
@RequiredArgsConstructor
public class JwtController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/newtoken")
    @ResponseBody
    public ResponseEntity<CommonResponse<TokenDto>> issueAccessToken(@RequestBody TokenDto refreshToken) {
        String kakaoId = userService.verifyToken(refreshToken.getRefreshToken());
        String token = jwtTokenService.createRefreshToken(kakaoId);
        return new ResponseEntity<>(new CommonResponse<>(new TokenDto(token), CommonResponseStatus.SUCCESS),HttpStatus.OK);
    }

    @GetMapping("/refresh-token-validity")
    @ResponseBody
    public ResponseEntity<CommonResponse<CommonResponseStatus>> loginCheck(
            @RequestHeader("Authorization") String refreshToken) {
        jwtTokenProvider.extractIDs(refreshToken.substring(7));
        return new ResponseEntity<>(new CommonResponse<>(), HttpStatus.OK);
    }
}
