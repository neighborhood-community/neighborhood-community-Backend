package project.neighborhoodcommunity.jwtTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import project.neighborhoodcommunity.jwt.JwtTokenProvider;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class JwtTest {

    private RestDocumentationResultHandler restDocs;
    @Autowired private MockMvc mockMvc;
    @Autowired private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp(
            final WebApplicationContext context,
            final RestDocumentationContextProvider provider
    ) {
        this.restDocs = document("jwt/{method-name}",
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .alwaysDo(restDocs)
                .build();
    }

    @Test
    public void pass_Jwt() throws Exception {
        // Given
        String accessToken = jwtTokenProvider.createToken("1234567");

        // When

        // Then
        mockMvc.perform(get("/jwt")
                        .header(HttpHeaders.HOST, "43.202.6.185:8080")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        )
                ));
    }

    @Test
    public void accessNotJwt() throws Exception {
        //Given
        String accessToken = "";

        //When

        //Then
        tokenTest(accessToken);
    }

    @Test
    public void expiredJwt() throws Exception {
        //Given
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyOTI3MjM5NTU5IiwiZXhwIjoxNjkwMzgyMzQxfQ" +
                ".cdGDZuH2cYxi5dlOZwg9NWP0xoV54kLhcQa-LkvYCCggeYbbZkoft_WKewHRiIAPxifjY2_jYHqEQ6a6F-gwlw";
        //When

        //Then
        tokenTest(accessToken);
    }

    @Test
    public void illegalJwt() throws Exception {
        //Given
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyOTI3MjM5NTU5IiwiZXhwIjoxNjkwMzgyMzQxfQ" +
                ".cdGDZuH2cYxi5dlOZwg9NWP0xoV54kLccQa-LkvYCCggeYbbZkoft_WKewHRiIAPxifjY2_jYHqEQ6a6F-gwlw";

        //When

        //Then
        tokenTest(accessToken);
    }

    private void tokenTest(String accessToken) throws Exception {
        mockMvc.perform(get("/jwt")
                        .header(HttpHeaders.HOST, "43.202.6.185:8080")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isUnauthorized())
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("code").description("Http 상태 코드"),
                                fieldWithPath("message").description("상태 메시지")
                        )
                ));
    }
}
