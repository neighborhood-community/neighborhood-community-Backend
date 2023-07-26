package project.neighborhoodcommunity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.neighborhoodcommunity.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKakaoid(String kakaoId);
    boolean existsByKakaoidAndRefreshToken(String kakaoId, String refreshToken);
}
