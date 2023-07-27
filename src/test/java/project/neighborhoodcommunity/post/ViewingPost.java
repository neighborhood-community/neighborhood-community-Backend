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
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(RestDocsConfiguration.class)
public class ViewingPost {

    @Autowired private RestDocumentationResultHandler restDocs;
    @Autowired private MockMvc mockMvc;

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
    void searchPost() throws Exception {
        //When & Then
        mockMvc.perform(get("/post")
                        .header(HttpHeaders.HOST, "43.202.6.185:8080")
                        .queryParam("category", "all")
                        .queryParam("page", "1"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
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
                                fieldWithPath("category").description("카테고리"),
                                fieldWithPath("region").description("지역"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("tags").description("태그 (,)로 구분"),
                                fieldWithPath("nickname").description("글쓴이")
                        )
                ));
    }
}
