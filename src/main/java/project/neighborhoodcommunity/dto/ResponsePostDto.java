package project.neighborhoodcommunity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResponsePostDto {

    private int totalPages;
    private List<Posts> posts;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Posts {
        private Long id;
        private String title;
        private String category;
        private String region;
        private String content;
        private String nickname;
        private String createdAt;

        public Posts(final project.neighborhoodcommunity.entity.Post post, final String nickname) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.category = post.getCategory();
            this.region = post.getRegion();
            this.content = post.getContent();
            this.nickname = nickname;
            this.createdAt = post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    public ResponsePostDto(final int totalPages, final List<ResponsePostDto.Posts> post) {
        this.totalPages = totalPages;
        this.posts = post;
    }
}
