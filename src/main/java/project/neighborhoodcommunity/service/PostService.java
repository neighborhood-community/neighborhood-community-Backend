package project.neighborhoodcommunity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.constant.CommonResponseStatus;
import project.neighborhoodcommunity.dto.RequestPostDto;
import project.neighborhoodcommunity.dto.ResponsePostDto;
import project.neighborhoodcommunity.entity.Post;
import project.neighborhoodcommunity.entity.User;
import project.neighborhoodcommunity.exception.AccessDeniedException;
import project.neighborhoodcommunity.exception.NotFoundException;
import project.neighborhoodcommunity.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public ResponsePostDto getAllPost(Pageable pageable) {
        Page<Post> posts = postRepository.findAllWithUser(pageable);
        return convertPostsToDto(posts);
    }

    public ResponsePostDto getAllPostByCategory(String category, Pageable pageable) {
        Page<Post> posts = postRepository.findAllByCategory(category, pageable);
        return convertPostsToDto(posts);
    }

    // ----------- search Post ------------

    public ResponsePostDto getAllPostByKakaoId(String kakaoId, Pageable pageable) {
        Page<Post> posts = postRepository.findByUserKakaoid(kakaoId, pageable);
        return convertPostsToDto(posts);
    }

    public ResponsePostDto getAllPostByKakaoIdAndCategory(String kakaoId, String category, Pageable pageable) {
        Page<Post> posts = postRepository.findAllByUserKakaoidAndCategory(kakaoId, category, pageable);
        return convertPostsToDto(posts);
    }

    private ResponsePostDto convertPostsToDto(Page<Post> posts) {
        List<ResponsePostDto.Posts> postDtoList = posts
                .map(post -> new ResponsePostDto.Posts(post, post.getUser()))
                .getContent();
        return new ResponsePostDto(posts.getTotalPages(), postDtoList);
    }

    // ----------- search MyPost -------------

    public void deleteMyPost(String kakaoId, Long id) {
        Post post = findById(id);

        if (!post.getUser().getKakaoid().equals(kakaoId))
            throw new AccessDeniedException(CommonResponseStatus.UNEQUAL_USER);

        postRepository.deleteById(id);
    }

    // ----------- delete Post -------------

    public void updatePost(RequestPostDto requestPostDto, String kakaoId, Long id) {
        Post post = findById(id);
        post.update(requestPostDto, kakaoId, id);
        postRepository.save(post);
    }

    // ------------ insert Post ------------
    //
    public void insertPost(RequestPostDto requestPostDto, String kakaoId) {
        User user = userService.findBykakaoId(kakaoId);
        Post post = new Post(requestPostDto, user);
        postRepository.save(post);
    }

    public RequestPostDto getPostById(Long id) {
        Post post = findById(id);
        return new RequestPostDto(post, post.getUser());
    }

    private Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CommonResponseStatus.NOT_FOUND_POST));
    }
}
