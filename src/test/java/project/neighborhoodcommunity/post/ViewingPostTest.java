package project.neighborhoodcommunity.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import project.neighborhoodcommunity.RestDocsConfiguration;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(RestDocsConfiguration.class)
public class ViewingPostTest {

    @Autowired private RestDocumentationResultHandler restDocs;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(
            final WebApplicationContext context,
            final RestDocumentationContextProvider provider
    ) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(restDocs)
                .build();
    }

    @Test
    void getPosts() throws Exception {
        //When & Then
        mockMvc.perform(get("/posts")
                        .header(HttpHeaders.HOST, "43.202.118.132")
                        .queryParam("category", "all")
                        .queryParam("perPage", "15")
                        .queryParam("page", "1"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("category").description("""
                                        모두 : all +
                                        산책 : walk +
                                        영화 : movie +
                                        술 : alcohol +
                                        운동 : exercise +
                                        독서 : reading +
                                        공부 : study"""),
                                parameterWithName("perPage").description("1페이지당 몇 개의 게시글을 보여줄지 결정"),
                                parameterWithName("page").description("n 페이지")
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
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("region").description("지역"),
                                fieldWithPath("profileImg").description("프로필 이미지"),
                                fieldWithPath("nickname").description("글쓴이"),
                                fieldWithPath("gender").description("글쓴이 성별"),
                                fieldWithPath("createdAt").description("작성 시간")
                        )
                ));
    }

    @Test
    void getOnePost() throws Exception {
        //When & Then
        mockMvc.perform(get("/post/{id}", 1)
                        .header(HttpHeaders.HOST, "43.202.118.132"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("id").description("게시글 번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("Http 상태 코드"),
                                fieldWithPath("message").description("상태 메시지")
                        ).andWithPrefix("data.",
                                fieldWithPath("id").description("게시글 번호"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("category").description("카테고리"),
                                fieldWithPath("content").description("글 내용"),
                                fieldWithPath("region").description("지역"),
                                fieldWithPath("profileImg").description("프로필 이미지"),
                                fieldWithPath("nickname").description("글쓴이 닉네임"),
                                fieldWithPath("gender").description("글쓴이 성별"),
                                fieldWithPath("createdAt").description("작성 시간")
                        )
                ));
    }
}
