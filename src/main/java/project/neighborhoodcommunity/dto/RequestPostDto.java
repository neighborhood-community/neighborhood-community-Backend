package project.neighborhoodcommunity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.neighborhoodcommunity.entity.Post;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestPostDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String title;
    private String category;
    private String region;
    private String content;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createdAt;

    public RequestPostDto(Post post, String nickname) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.region = post.getRegion();
        this.content = post.getContent();
        this.nickname = nickname;
        this.createdAt = post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
