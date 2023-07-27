package project.neighborhoodcommunity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {

    private int totalPages;
    private List<Posts> posts;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Posts {
        private Long id;
        private String category;
        private String region;
        private String content;
        private String tags;
        private String nickname;

        public Posts(project.neighborhoodcommunity.entity.Post post, String nickname) {
            this.id = post.getId();
            this.category = post.getCategory();
            this.region = post.getRegion();
            this.content = post.getContent();
            this.tags = post.getTags();
            this.nickname = nickname;
        }
    }

    public PostDto(int totalPages, List<PostDto.Posts> post) {
        this.totalPages = totalPages;
        this.posts = post;
    }
}
