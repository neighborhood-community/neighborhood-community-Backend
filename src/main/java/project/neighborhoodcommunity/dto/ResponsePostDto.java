package project.neighborhoodcommunity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.neighborhoodcommunity.entity.User;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
        private String category;
        private String title;
        private String content;
        private String region;
        private String profileImg;
        private String nickname;
        private String gender;
        private String createdAt;


        public Posts(final project.neighborhoodcommunity.entity.Post post, final User user) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.category = post.getCategory();
            this.content = post.getContent();
            this.region = post.getRegion();
            this.profileImg = user.getProfile_img();
            this.nickname = user.getNickname();
            this.gender = String.valueOf(user.getGender());
            this.createdAt = post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    public ResponsePostDto(final int totalPages, final List<ResponsePostDto.Posts> post) {
        this.totalPages = totalPages;
        this.posts = post;
    }
}
