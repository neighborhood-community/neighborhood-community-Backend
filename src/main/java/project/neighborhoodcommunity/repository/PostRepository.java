package project.neighborhoodcommunity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.neighborhoodcommunity.dto.PostDto;
import project.neighborhoodcommunity.entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p join fetch p.user")
    List<PostDto> findAllWithUser();
}
