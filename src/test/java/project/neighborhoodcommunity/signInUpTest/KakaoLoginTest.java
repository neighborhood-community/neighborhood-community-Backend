package project.neighborhoodcommunity.signInUpTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import project.neighborhoodcommunity.dto.TokenDto;
import project.neighborhoodcommunity.dto.RequestSignUpDto;
import project.neighborhoodcommunity.entity.User;
import project.neighborhoodcommunity.service.KakaoAccessTokenProviderService;
import project.neighborhoodcommunity.service.KakaoLoginService;
import project.neighborhoodcommunity.service.KakaoUserInfoProviderService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class KakaoLoginTest {
    private RestDocumentationResultHandler restDocs;
    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "kakaoAccessTokenProviderService")
    private KakaoAccessTokenProviderService kakaoAccessTokenProviderService;

    @MockBean(name = "kakaoUserInfoProviderService")
    private KakaoUserInfoProviderService kakaoUserInfoProviderService;

    @MockBean
    private KakaoLoginService kakaoLoginService;

    @BeforeEach
    void setUp(
            final WebApplicationContext context,
            final RestDocumentationContextProvider provider
    ) {
        this.restDocs = document("kakao-login/oauth/{method-name}",
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(restDocs)
                .build();
    }

    @Test
    public void kakaoLoginSuccess() throws Exception {
        // Given
        RequestSignUpDto requestSignUpDto = new RequestSignUpDto();
        requestSignUpDto.setEmail("aossuper7@naver.com");
        requestSignUpDto.setNickname("박정민");
        requestSignUpDto.setProfileImg("http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg");
        TokenDto tokenDto = new TokenDto(
                "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhb3NzdXBlcjdAbmF2ZXIuY29tIiwiZXhwIjoxNjkwMTI3NDk5fQ.R2j5PVz57YpOzTthYzNcrbRlYgy4ddSFv5hxm14o1PzxRhYOX3AVH2wNkDZrLJzdoTgPoZ67CYgNTlOyjdXcOw",
                "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhb3NzdXBlcjdAbmF2ZXIuY29tIiwiZXhwIjoxNjkxMzM1Mjk5fQ.c-NdhuORzcMBWAr2NscETTeI8uujEuMVmg8NFA0HjA9oS6kO8nECnlO31hOPm8sw0LJVcIMK1yPAlCtJTh29Yw");

        // When
        when(kakaoAccessTokenProviderService.getAccessToken(any())).thenReturn("test_access_token");
        when(kakaoUserInfoProviderService.getUserInfo(any())).thenReturn(requestSignUpDto);
        when(kakaoLoginService.login(any())).thenReturn(Optional.of(new User()));
        when(kakaoLoginService.loginSuccessToken(any(User.class))).thenReturn(tokenDto);

        // Then
        mockMvc.perform(get("/kakao").param("code", "{AuthorizationCode}"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(restDocs.document(
                        RequestDocumentation.queryParameters(
                                RequestDocumentation.parameterWithName("code").description("Kakao authorization code")
                        ),
                        PayloadDocumentation.responseFields(
                                fieldWithPath("code").description("Http 상태 코드"),
                                fieldWithPath("message").description("상태 메시지"),
                                fieldWithPath("data.accessToken").description("액세스 토큰"),
                                fieldWithPath("data.refreshToken").description("리프레시 토큰")
                        )));
    }

    @Test
    public void kakaoLoginFailure() throws Exception {
        // Given
        RequestSignUpDto requestSignUpDto = new RequestSignUpDto();
        requestSignUpDto.setEmail("aossuper7@naver.com");
        requestSignUpDto.setNickname("박정민");
        requestSignUpDto.setProfileImg("http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg");

        // When
        when(kakaoAccessTokenProviderService.getAccessToken(any())).thenReturn("test_access_token");
        when(kakaoUserInfoProviderService.getUserInfo(any())).thenReturn(requestSignUpDto);
        when(kakaoLoginService.login(any())).thenReturn(Optional.empty());

        // Then
        mockMvc.perform(get("/kakao").param("code", "{AuthorizationCode}"))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(restDocs.document(
                        RequestDocumentation.queryParameters(
                                RequestDocumentation.parameterWithName("code").description("Kakao authorization code")
                        ),
                        PayloadDocumentation.responseFields(
                                fieldWithPath("code").description("Http 상태 코드"),
                                fieldWithPath("message").description("상태 메시지"),
                                fieldWithPath("data.email").description("사용자 Email"),
                                fieldWithPath("data.nickname").description("사용자 nickname"),
                                fieldWithPath("data.profileImg").description("사용자 프로필 사진")
                        )));
    }
}
