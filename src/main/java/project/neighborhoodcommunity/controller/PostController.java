package project.neighborhoodcommunity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.constant.CommonResponse;
import project.constant.CommonResponseStatus;
import project.neighborhoodcommunity.dto.RequestPostDto;
import project.neighborhoodcommunity.dto.ResponsePostDto;
import project.neighborhoodcommunity.service.PostService;

import static project.constant.CommonResponseStatus.*;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    /*
     * 게시글 조회 API
     */

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<CommonResponse<ResponsePostDto>> searchPost(String category, int page) {
        if(category.equals("all")) {
            ResponsePostDto posts = postService.getAllPost(PageRequest.of(page - 1, 8));
            return new ResponseEntity<>(new CommonResponse<>(posts, SUCCESS), OK);
        }
        ResponsePostDto posts = postService.getAllPostByCategory(category, PageRequest.of(page - 1, 8));
        return new ResponseEntity<>(new CommonResponse<>(posts, SUCCESS), OK);
    }

    /*
     * 내가 쓴 게시글 조회 API
     */

    @GetMapping("/my")
    @ResponseBody
    public ResponseEntity<CommonResponse<ResponsePostDto>> searchMyPost(int page) {
        String kakaoId = SecurityContextHolder.getContext().getAuthentication().getName();
        ResponsePostDto posts = postService.getAllPostByKakaoId(kakaoId, PageRequest.of(page - 1, 8));
        return new ResponseEntity<>(new CommonResponse<>(posts, SUCCESS), OK);
    }

    /*
     * 게시글 삭제 API
     */

    @DeleteMapping("/d")
    @ResponseBody
    public ResponseEntity<CommonResponse<CommonResponseStatus>> deleteMyPost(long id) {
        String kakaoId = SecurityContextHolder.getContext().getAuthentication().getName();
        postService.deleteMyPost(kakaoId, id);
        return new ResponseEntity<>(new CommonResponse<>(), OK);
    }

    /*
     * 게시글 수정 API
     */

    @PatchMapping("/u")
    @ResponseBody
    public ResponseEntity<CommonResponse<CommonResponseStatus>> updatePost(@RequestBody RequestPostDto requestPostDto) {
        String kakaoId = SecurityContextHolder.getContext().getAuthentication().getName();
        postService.updatePost(requestPostDto, kakaoId);
        return new ResponseEntity<>(new CommonResponse<>(), OK);
    }

    /*
     * 게시글 추가 API
     */

    @PostMapping("/i")
    @ResponseBody
    public ResponseEntity<CommonResponse<CommonResponseStatus>> insertPost(@RequestBody RequestPostDto requestPostDto) {
        String kakaoId = SecurityContextHolder.getContext().getAuthentication().getName();
        postService.insertPost(requestPostDto, kakaoId);
        return new ResponseEntity<>(new CommonResponse<>(), OK);
    }
}
