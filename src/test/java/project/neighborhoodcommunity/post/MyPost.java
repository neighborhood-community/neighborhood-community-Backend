package project.neighborhoodcommunity.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import project.neighborhoodcommunity.RestDocsConfiguration;
import project.neighborhoodcommunity.dto.RequestPostDto;
import project.neighborhoodcommunity.jwt.JwtTokenProvider;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Transactional
@Import(RestDocsConfiguration.class)
public class MyPost {

    @Autowired private RestDocumentationResultHandler restDocs;
    @Autowired private MockMvc mockMvc;
    @Autowired private JwtTokenProvider jwtTokenProvider;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(
            final WebApplicationContext context,
            final RestDocumentationContextProvider provider
    ) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .alwaysDo(restDocs)
                .build();
    }

    @Test
    void searchMyPostAll() throws Exception {
        //Given
        String accessToken = jwtTokenProvider.createToken("2927239559");

        //When & Then
        mockMvc.perform(get("/post/my")
                        .header(HttpHeaders.HOST, "43.202.6.185:8080")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .queryParam("page", "1"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        ),
                        queryParameters(
                                parameterWithName("page").description("1페이지당 8개의 게시글")
                        ),
                        responseFields(
                                fieldWithPath("code").description("Http 상태코드"),
                                fieldWithPath("message").description("요청에 성공하였습니다."),
                                fieldWithPath("data.totalPages").description("총 페이지 수"),
                                fieldWithPath("data.posts[]").description("게시글 리스트")
                        ).andWithPrefix("data.posts[].",
                                fieldWithPath("id").description("게시글 번호"),
                                fieldWithPath("category").description("카테고리"),
                                fieldWithPath("region").description("지역"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("tags").description("태그 (, )로 구분"),
                                fieldWithPath("nickname").description("글쓴이")
                        )
                ));
    }

    @Test
    void deleteMyPost() throws Exception {
        //Given
        String accessToken = jwtTokenProvider.createToken("2927239559");

        //When & Then
        mockMvc.perform(delete("/post/d")
                        .header(HttpHeaders.HOST, "43.202.6.185:8080")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .queryParam("id", "4"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        ),
                        queryParameters(
                                parameterWithName("id").description("게시글 번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("Http 상태 코드"),
                                fieldWithPath("message").description("상태 메시지")
                        )
                ));
    }

    @Test
    void updatePost() throws Exception {
        //Given
        String accessToken = jwtTokenProvider.createToken("2927239559");
        RequestPostDto postDto = new RequestPostDto();
        postDto.setId(2L);
        postDto.setCategory("movie");
        postDto.setRegion("서울시");
        postDto.setContent("testtest");
        postDto.setTags("test1");

        //When & Then
        mockMvc.perform(patch("/post/u")
                        .header(HttpHeaders.HOST, "43.202.6.185:8080")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        ),
                        requestFields(
                                fieldWithPath("id").description("게시글 번호"),
                                fieldWithPath("category").description("카테고리"),
                                fieldWithPath("region").description("지역"),
                                fieldWithPath("content").description("글 내용"),
                                fieldWithPath("tags").description("태크 (, )로 구분")
                        )
                ));
    }

    @Test
    void insertPost() throws Exception {
        //Given
        String accessToken = jwtTokenProvider.createToken("2927239559");
        RequestPostDto postDto = new RequestPostDto();
        postDto.setCategory("movie");
        postDto.setRegion("서울시");
        postDto.setContent("testtest");
        postDto.setTags("test1");

        //When & Then
        mockMvc.perform(post("/post/i")
                        .header(HttpHeaders.HOST, "43.202.6.185:8080")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        ),
                        requestFields(
                                fieldWithPath("category").description("카테고리"),
                                fieldWithPath("content").description("글 내용"),
                                fieldWithPath("region").description("지역"),
                                fieldWithPath("tags").description("태크 (, )로 구분")
                        )
                ));
    }
}
