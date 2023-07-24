package project.neighborhoodcommunity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestSignUpDto {

    private String kakaoid;
    private String email;
    private String nickname;
    private String profile_img;
}
