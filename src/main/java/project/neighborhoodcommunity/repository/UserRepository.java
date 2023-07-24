package project.neighborhoodcommunity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.neighborhoodcommunity.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByKakaoid(String kakaoId);
}
