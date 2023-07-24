package project.neighborhoodcommunity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.neighborhoodcommunity.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
