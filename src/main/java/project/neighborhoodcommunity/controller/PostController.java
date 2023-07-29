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
public class PostController {

    private final PostService postService;

    /*
     * 게시글 조회 API
     */

    @GetMapping("/posts")
    @ResponseBody
    public ResponseEntity<CommonResponse<ResponsePostDto>> getPosts(String category, int page) {
        if(category.equals("all")) {
            ResponsePostDto posts = postService.getAllPost(PageRequest.of(page - 1, 8));
            return new ResponseEntity<>(new CommonResponse<>(posts, SUCCESS), OK);
        }
        ResponsePostDto posts = postService.getAllPostByCategory(category, PageRequest.of(page - 1, 8));
        return new ResponseEntity<>(new CommonResponse<>(posts, SUCCESS), OK);
    }

    /*
     * 해당 게시글 조회
     */

    @GetMapping("/post/{id}")
    @ResponseBody
    public ResponseEntity<CommonResponse<RequestPostDto>> viewingPost(@PathVariable Long id) {
        return new ResponseEntity<>(new CommonResponse<>(postService.getPostById(id), SUCCESS),OK);
    }

    /*
     * 내가 쓴 게시글 조회 API
     */

    @GetMapping("/posts/my")
    @ResponseBody
    public ResponseEntity<CommonResponse<ResponsePostDto>> searchMyPost(int page) {
        String kakaoId = SecurityContextHolder.getContext().getAuthentication().getName();
        ResponsePostDto posts = postService.getAllPostByKakaoId(kakaoId, PageRequest.of(page - 1, 8));
        return new ResponseEntity<>(new CommonResponse<>(posts, SUCCESS), OK);
    }

    /*
     * 게시글 삭제 API
     */

    @DeleteMapping("/post/d/{id}")
    @ResponseBody
    public ResponseEntity<CommonResponse<CommonResponseStatus>> deleteMyPost(@PathVariable long id) {
        String kakaoId = SecurityContextHolder.getContext().getAuthentication().getName();
        postService.deleteMyPost(kakaoId, id);
        return new ResponseEntity<>(new CommonResponse<>(), OK);
    }

    /*
     * 게시글 수정 API
     */

    @PatchMapping("/post/u/{id}")
    @ResponseBody
    public ResponseEntity<CommonResponse<CommonResponseStatus>> updatePost(
            @PathVariable Long id,
            @RequestBody RequestPostDto requestPostDto
    ) {
        String kakaoId = SecurityContextHolder.getContext().getAuthentication().getName();
        postService.updatePost(requestPostDto, kakaoId, id);
        return new ResponseEntity<>(new CommonResponse<>(), OK);
    }

    /*
     * 게시글 추가 API
     */

    @PostMapping("/post/i")
    @ResponseBody
    public ResponseEntity<CommonResponse<CommonResponseStatus>> insertPost(@RequestBody RequestPostDto requestPostDto) {
        String kakaoId = SecurityContextHolder.getContext().getAuthentication().getName();
        postService.insertPost(requestPostDto, kakaoId);
        return new ResponseEntity<>(new CommonResponse<>(), OK);
    }
}
