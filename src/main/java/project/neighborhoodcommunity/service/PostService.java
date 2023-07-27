package project.neighborhoodcommunity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.neighborhoodcommunity.dto.PostDto;
import project.neighborhoodcommunity.entity.Post;
import project.neighborhoodcommunity.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostDto getAllPost(Pageable pageable) {
        Page<Post> posts = postRepository.findAllWithUser(pageable);
        return convertPostsToDto(posts);
    }

    public PostDto getAllPostByCategory(String category, Pageable pageable) {
        Page<Post> posts = postRepository.findAllByCategory(category, pageable);
        return convertPostsToDto(posts);
    }

    private PostDto convertPostsToDto(Page<Post> posts) {
        List<PostDto.Posts> postDtoList = posts
                .map(post -> new PostDto.Posts(post, post.getUser().getNickname()))
                .getContent();
        return new PostDto(posts.getTotalPages(), postDtoList);
    }
}
