package project.neighborhoodcommunity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import project.constant.CommonResponse;
import project.neighborhoodcommunity.dto.PostDto;
import project.neighborhoodcommunity.service.PostService;

import static project.constant.CommonResponseStatus.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<CommonResponse<PostDto>> searchPost(String category, int page) {
        if(category.equals("all")) {
            PostDto posts = postService.getAllPost(PageRequest.of(page - 1, 8));
            return new ResponseEntity<>(new CommonResponse<>(posts, SUCCESS), HttpStatus.OK);
        }
        PostDto posts = postService.getAllPostByCategory(category, PageRequest.of(page - 1, 8));
        return new ResponseEntity<>(new CommonResponse<>(posts, SUCCESS), HttpStatus.OK);
    }
}
