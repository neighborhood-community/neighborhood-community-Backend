package project.neighborhoodcommunity.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import project.constant.CommonResponse;
import project.constant.CommonResponseStatus;
import project.neighborhoodcommunity.exception.UnsuitableJwtException;

import java.io.IOException;

@AllArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String jwt = resolveToken(httpServletRequest.getHeader("Authorization"));

        if (StringUtils.hasText(jwt)) {
            try {
                jwtTokenProvider.validateToken(jwt);
                Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            } catch (UnsuitableJwtException e) {
                sendResponse(httpServletResponse, CommonResponseStatus.UNSUITABLE_JWT);
            } catch (ExpiredJwtException e) {
                sendResponse(httpServletResponse, CommonResponseStatus.EXPIRED_JWT);
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
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    private String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        return null;
    }
}
