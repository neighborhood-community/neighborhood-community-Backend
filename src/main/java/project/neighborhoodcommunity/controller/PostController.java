package project.neighborhoodcommunity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import project.constant.CommonResponse;
import project.constant.CommonResponseStatus;
import project.neighborhoodcommunity.dto.PostDto;
import project.neighborhoodcommunity.entity.Post;
import project.neighborhoodcommunity.service.PostService;

import java.util.List;

import static project.constant.CommonResponseStatus.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<CommonResponse<List<PostDto>>> searchPost(String category) {
        System.out.println("여기 실행 됨?");
        List<PostDto> post = postService.getAllPost();
        return new ResponseEntity<>(new CommonResponse<>(post, SUCCESS), HttpStatus.OK);
//        if (category.equals("all"))
//        else
//            post = postService.getPostByCategory(category);
//
//        return new ResponseEntity<>(new CommonResponse<>(post, SUCCESS), HttpStatus.OK);
    }

}
