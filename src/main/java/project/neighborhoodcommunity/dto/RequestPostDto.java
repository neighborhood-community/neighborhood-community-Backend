package project.neighborhoodcommunity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
