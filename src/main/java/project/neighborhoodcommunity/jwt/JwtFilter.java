package project.neighborhoodcommunity.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import project.constant.CommonResponse;
import project.constant.CommonResponseStatus;
import project.neighborhoodcommunity.exception.AccessDeniedException;
import project.neighborhoodcommunity.exception.ExpiredJwtTokenException;
import project.neighborhoodcommunity.exception.UnsuitableJwtException;

import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Set<String> securedPaths = Set.of("/posts/my", "/post/d/", "/post/u/", "/post/i"
            ,"/mypage", "/mypage/posts");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String path = ((HttpServletRequest) request).getRequestURI();
        if (securedPaths.stream().anyMatch(path::startsWith)) {
            try {
                String jwt = resolveToken(httpServletRequest.getHeader("Authorization"));
                jwtTokenProvider.validateToken(jwt);
                Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            } catch (UnsuitableJwtException e) {
                sendResponse(httpServletResponse, CommonResponseStatus.UNSUITABLE_JWT);
            } catch (ExpiredJwtTokenException e) {
                sendResponse(httpServletResponse, CommonResponseStatus.EXPIRED_JWT);
            } catch (AccessDeniedException e) {
                sendResponse(httpServletResponse, CommonResponseStatus.UNAUTHORIZED);
            }
        } else
            chain.doFilter(request, response);
    }

    private void sendResponse(HttpServletResponse response, CommonResponseStatus status) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(convertObjectToJson(new CommonResponse<>(status)));
    }

    private String convertObjectToJson(Object object) throws IOException {
        if (object == null)
            return null;
        return objectMapper.writeValueAsString(object);
    }

    private String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        throw new AccessDeniedException(CommonResponseStatus.UNAUTHORIZED);
    }
}
