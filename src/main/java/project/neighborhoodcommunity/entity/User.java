package project.neighborhoodcommunity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String kakaoid;
    private String email;
    private String nickname;
    private String profile_img;
    private String refreshToken;

    @OneToMany(mappedBy = "user")
    private List<Post> post;
}
