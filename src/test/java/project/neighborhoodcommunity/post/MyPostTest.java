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
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Transactional
@Import(RestDocsConfiguration.class)
public class MyPostTest {

    @Autowired private RestDocumentationResultHandler restDocs;
    private MockMvc mockMvc;
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
    void getMyPostAll() throws Exception {
        //Given
        String accessToken = jwtTokenProvider.createToken("2927239559");

        //When & Then
        mockMvc.perform(get("/posts/my")
                        .header(HttpHeaders.HOST, "43.202.118.132")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .queryParam("category", "all")
                        .queryParam("page", "1"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        ),
                        queryParameters(
                                parameterWithName("category").description("""
                                        모두 : all +
                                        반려동물 산책 : pet +
                                        영화 : movie +
                                        런닝 : running +
                                        헬스/필라테스/요가 : exercise"""),
                                parameterWithName("page").description("1페이지당 8개의 게시글")
                        ),
                        responseFields(
                                fieldWithPath("code").description("Http 상태코드"),
                                fieldWithPath("message").description("요청에 성공하였습니다."),
                                fieldWithPath("data.totalPages").description("총 페이지 수"),
                                fieldWithPath("data.posts[]").description("게시글 리스트")
                        ).andWithPrefix("data.posts[].",
                                fieldWithPath("id").description("게시글 번호"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("category").description("카테고리"),
                                fieldWithPath("region").description("지역"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("nickname").description("글쓴이"),
                                fieldWithPath("createdAt").description("작성 시간")
                        )
                ));
    }

    @Test
    void deleteMyPost() throws Exception {
        //Given
        String accessToken = jwtTokenProvider.createToken("2927239559");

        //When & Then
        mockMvc.perform(delete("/post/d/{id}", 4)
                        .header(HttpHeaders.HOST, "43.202.118.132")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        ),
                        pathParameters(
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
        postDto.setTitle("영화 추천");
        postDto.setCategory("movie");
        postDto.setRegion("서울시");
        postDto.setContent("영화 추천해주세요 넷플릭스, 현재 상영 영화 상관 없습니다.");

        //When & Then
        mockMvc.perform(patch("/post/u/{id}", 2)
                        .header(HttpHeaders.HOST, "43.202.118.132")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        ),
                        pathParameters(
                                parameterWithName("id").description("게시글 번호")
                        ),
                        requestFields(
                                fieldWithPath("category").description("카테고리"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("region").description("지역"),
                                fieldWithPath("content").description("글 내용")
                        )
                ));
    }

    @Test
    void insertPost() throws Exception {
        //Given
        String accessToken = jwtTokenProvider.createToken("2927239559");
        RequestPostDto postDto = new RequestPostDto();
        postDto.setTitle("영화 추천");
        postDto.setCategory("movie");
        postDto.setRegion("서울시");
        postDto.setContent("영화 추천해주세요 넷플릭스, 현재 상영 영화 상관 없습니다.");

        //When & Then
        mockMvc.perform(post("/post/i")
                        .header(HttpHeaders.HOST, "43.202.118.132")
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
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("글 내용"),
                                fieldWithPath("region").description("지역")
                        )
                ));
    }
}
