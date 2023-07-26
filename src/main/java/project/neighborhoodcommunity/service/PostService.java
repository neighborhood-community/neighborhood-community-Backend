package project.neighborhoodcommunity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.neighborhoodcommunity.dto.PostDto;
import project.neighborhoodcommunity.entity.Post;
import project.neighborhoodcommunity.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<PostDto> getAllPost() {
         return postRepository.findAllWithUser();
    }

//    public List<Post> getPostByCategory(String category) {
//        return postRepository.findByCategory(category);
//    }
}
