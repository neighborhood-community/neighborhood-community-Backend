package project.neighborhoodcommunity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.neighborhoodcommunity.entity.Post;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestPostDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String category;
    private String region;
    private String content;
    private String tags;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;

    public RequestPostDto(Post post, String nickname) {
        this.id = post.getId();
        this.category = post.getCategory();
        this.region = post.getRegion();
        this.content = post.getContent();
        this.tags = post.getTags();
        this.nickname = nickname;
    }
}
