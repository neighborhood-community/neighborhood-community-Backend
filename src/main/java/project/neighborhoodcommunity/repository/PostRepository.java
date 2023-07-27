package project.neighborhoodcommunity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.neighborhoodcommunity.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"user"})
    @Query("select p from Post p")
    Page<Post> findAllWithUser(Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    @Query("select p from Post p where p.category = :category")
    Page<Post> findAllByCategory(@Param("category") String category, Pageable pageable);
}
