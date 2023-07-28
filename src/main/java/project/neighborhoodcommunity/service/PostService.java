package project.neighborhoodcommunity.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
import project.neighborhoodcommunity.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

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

    private ResponsePostDto convertPostsToDto(Page<Post> posts) {
        List<ResponsePostDto.Posts> postDtoList = posts
                .map(post -> new ResponsePostDto.Posts(post, post.getUser().getNickname()))
                .getContent();
        return new ResponsePostDto(posts.getTotalPages(), postDtoList);
    }

    // ----------- search MyPost -------------

    public void deleteMyPost(String kakaoId, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CommonResponseStatus.NOT_FOUND_POST));

        if (!post.getUser().getKakaoid().equals(kakaoId))
            throw new AccessDeniedException(CommonResponseStatus.UNEQUAL_USER);

        postRepository.deleteById(id);
    }

    // ----------- delete Post -------------

    public void updatePost(RequestPostDto requestPostDto, String kakaoId) {
        Post post = postRepository.findById(requestPostDto.getId())
                .orElseThrow(() -> new NotFoundException(CommonResponseStatus.NOT_FOUND_POST));

        post.update(requestPostDto, kakaoId);
        System.out.println();
        postRepository.save(post);
    }

    // ------------ insert Post ------------
    //
    public void insertPost(RequestPostDto requestPostDto, String kakaoId) {
        User user = userRepository.findByKakaoid(kakaoId)
                .orElseThrow(() -> new NotFoundException(CommonResponseStatus.NOT_FOUND_USER));
        Post post = new Post(requestPostDto, user);
        postRepository.save(post);
    }
}
