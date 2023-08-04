package project.neighborhoodcommunity.jwtTest;


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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import project.neighborhoodcommunity.RestDocsConfiguration;
import project.neighborhoodcommunity.dto.TokenDto;
import project.neighborhoodcommunity.entity.User;
import project.neighborhoodcommunity.jwt.JwtTokenProvider;
import project.neighborhoodcommunity.repository.UserRepository;

import java.util.Optional;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(RestDocsConfiguration.class)
public class ReRequestJwtTest {

    @Autowired private RestDocumentationResultHandler restDocs;
    private MockMvc mockMvc;
    @Autowired private JwtTokenProvider jwtTokenProvider;
    @Autowired private UserRepository userRepository;
    @Autowired private ObjectMapper objectMapper;

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
    void reRequestAccessTokenFailure() throws Exception {
        //Given
        String refreshToken = jwtTokenProvider.createRefreshToken("2927239559");
        TokenDto tokenDto = new TokenDto();
        tokenDto.setRefreshToken(refreshToken);
        //When & Then
        mockMvc.perform(post("/newtoken")
                        .header(HttpHeaders.HOST, "43.202.118.132")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenDto)))
                .andExpect(status().isNotFound())
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("code").description("Http 상태코드"),
                                fieldWithPath("message").description("상태 메시지")
                        )
                ));
    }

    @Test
    void reRequestAccessTokenSuccess() throws Exception {
        //Given
        Optional<User> user = userRepository.findByKakaoid("2927239559");
        TokenDto tokenDto = new TokenDto();
        String refreshToken = user.get().getRefreshToken();
        tokenDto.setRefreshToken(refreshToken);

        //When & Then
        mockMvc.perform(post("/newtoken")
                        .header(HttpHeaders.HOST, "43.202.118.132")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenDto)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("refreshToken").description("refreshToken")
                        ),
                        responseFields(
                                fieldWithPath("code").description("Http 상태 코드"),
                                fieldWithPath("message").description("상태 메시지"),
                                fieldWithPath("data.accessToken").description("재 발급된 AccessToken")
                        )
                ));
    }
}
