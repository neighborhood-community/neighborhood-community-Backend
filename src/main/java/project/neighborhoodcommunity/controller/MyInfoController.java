package project.neighborhoodcommunity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import project.constant.CommonResponse;
import project.constant.CommonResponseStatus;
import project.neighborhoodcommunity.dto.UserDto;
import project.neighborhoodcommunity.service.UserService;

import static project.constant.CommonResponseStatus.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyInfoController {

    private final UserService userService;

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<CommonResponse<UserDto>> getUserInfo() {
        String kakaoId = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(new CommonResponse<>(userService.getUserInfo(kakaoId), SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/u")
    @ResponseBody
    public ResponseEntity<CommonResponse<CommonResponseStatus>> updateInfo(@RequestBody UserDto userDto) {
        String kakaoId = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updateInfo(kakaoId, userDto);
        return new ResponseEntity<>(new CommonResponse<>(), HttpStatus.OK);
    }
}
