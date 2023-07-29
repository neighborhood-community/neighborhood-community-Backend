package project.neighborhoodcommunity.mypage;

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
import project.neighborhoodcommunity.dto.UserDto;
import project.neighborhoodcommunity.jwt.JwtTokenProvider;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Transactional
@Import(RestDocsConfiguration.class)
public class MyPageTest {

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
    void getMyInfo() throws Exception {
        //Given
        String accessToken = jwtTokenProvider.createToken("2927239559");

        //When & Then
        mockMvc.perform(get("/mypage")
                        .header(HttpHeaders.HOST, "43.202.6.185:8080")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        ),
                        responseFields(
                                fieldWithPath("code").description("Http 상태 코드"),
                                fieldWithPath("message").description("상태 메시지")
                        ).andWithPrefix("data.",
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("nickname").description("사용자 닉네임"),
                                fieldWithPath("profile_img").description("사용자 프로필 이미지"))
                ));
    }

    @Test
    void updateMyInfo() throws Exception {
        //Given
        String accessToken = jwtTokenProvider.createToken("2927239559");
        UserDto userDto = new UserDto();
        userDto.setEmail("aossuper8@naver.com");
        userDto.setNickname("aossuepr8");
        userDto.setProfile_img("test");

        //When & Then
        mockMvc.perform(patch("/mypage/u")
                        .header(HttpHeaders.HOST, "43.202.6.185:8080")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        ),
                        requestFields(
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("nickname").description("사용자 닉네임"),
                                fieldWithPath("profile_img").description("사용자 프로필 이미지")
                        )
                ));
    }
}
