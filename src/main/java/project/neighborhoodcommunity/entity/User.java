package project.neighborhoodcommunity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.neighborhoodcommunity.dto.UserDto;

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
    private String gender;
    private String nickname;
    private String profile_img;
    private String refreshToken;

    @OneToMany(mappedBy = "user")
    private List<Post> post;

    public void update(UserDto userDto) {
        this.gender = userDto.getGender();
        this.nickname = userDto.getNickname();
        this.profile_img = userDto.getProfile_img();
    }
}
