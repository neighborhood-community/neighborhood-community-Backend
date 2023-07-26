package project.neighborhoodcommunity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {

    private Long id;
    private String category;
    private String title;
    private String region;
    private String content;
    private String tags;
    private String nickname;
}
