package project.neighborhoodcommunity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import project.constant.CommonResponseStatus;
import project.neighborhoodcommunity.dto.RequestPostDto;
import project.neighborhoodcommunity.exception.AccessDeniedException;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    private String region;
    private String content;
    private String tags;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public Post(RequestPostDto postDto, User user) {
        this.category = postDto.getCategory();
        this.region = postDto.getRegion();
        this.content = postDto.getContent();
        this.tags = postDto.getTags();
        this.user = user;
    }

    public void update(final RequestPostDto postDto, final String kakaoId, final Long id) {
        if(!this.user.getKakaoid().equals(kakaoId))
            throw new AccessDeniedException(CommonResponseStatus.UNEQUAL_USER);

        this.id = id;
        this.category = postDto.getCategory();
        this.region = postDto.getRegion();
        this.content = postDto.getContent();
        this.tags = postDto.getTags();
    }
}
