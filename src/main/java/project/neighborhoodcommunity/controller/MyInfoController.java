package project.neighborhoodcommunity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.constant.CommonResponse;
import project.constant.CommonResponseStatus;
import project.neighborhoodcommunity.dto.ResponsePostDto;
import project.neighborhoodcommunity.dto.UserDto;
import project.neighborhoodcommunity.service.PostService;
import project.neighborhoodcommunity.service.UserService;

import static org.springframework.http.HttpStatus.OK;
import static project.constant.CommonResponseStatus.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyInfoController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<CommonResponse<UserDto>> getUserInfo() {
        String kakaoId = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(new CommonResponse<>(userService.getUserInfo(kakaoId), SUCCESS), HttpStatus.OK);
    }

    @PatchMapping("/u")
    @ResponseBody
    public ResponseEntity<CommonResponse<CommonResponseStatus>> updateInfo(@RequestBody UserDto userDto) {
        String kakaoId = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updateInfo(kakaoId, userDto);
        return new ResponseEntity<>(new CommonResponse<>(), HttpStatus.OK);
    }

    /*
     * 내가 쓴 게시글 카테고리별 조회 API
     */

    @GetMapping("/posts")
    @ResponseBody
    public ResponseEntity<CommonResponse<ResponsePostDto>> searchMyPost(String category, int perPage, int page) {
        String kakaoId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(category.equals("all")) {
            ResponsePostDto posts = postService.getAllPostByKakaoId(kakaoId, PageRequest.of(page - 1, perPage));
            return new ResponseEntity<>(new CommonResponse<>(posts, SUCCESS), OK);
        }
        ResponsePostDto posts = postService.getAllPostByKakaoIdAndCategory(
                kakaoId, category, PageRequest.of(page - 1, perPage));
        return new ResponseEntity<>(new CommonResponse<>(posts, SUCCESS), OK);
    }
}
