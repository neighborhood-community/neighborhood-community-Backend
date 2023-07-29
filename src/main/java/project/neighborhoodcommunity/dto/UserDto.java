package project.neighborhoodcommunity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.neighborhoodcommunity.entity.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String email;
    private String nickname;
    private String profile_img;

    public UserDto(User user) {
        this.email = user.getEmail();
        this.nickname = user.getEmail();
        this.profile_img = user.getProfile_img();
    }
}
