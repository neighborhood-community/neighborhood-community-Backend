package project.neighborhoodcommunity.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import project.constant.CommonResponseStatus;
import project.neighborhoodcommunity.exception.ExpiredJwtTokenException;
import project.neighborhoodcommunity.exception.InvalidJwtTokenException;
import project.neighborhoodcommunity.exception.UnsuitableJwtException;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

@Component
public class JwtTokenProvider implements InitializingBean {

    private final String secret;
    private final String refreshSecret;
    private final long tokenValidityInSeconds;
    private final long refreshTokenValidityInSeconds;
    private Key secretKey;
    private Key refreshSecretKey;
    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.refresh-secret}") String refreshSecret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds) {
        this.secret = secret;
        this.refreshSecret = refreshSecret;
        this.tokenValidityInSeconds = tokenValidityInSeconds * 1000;
        this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds * 1000;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        keyBytes = Decoders.BASE64.decode(refreshSecret);
        this.refreshSecretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String username) {
        return createJwtToken(username, secretKey, tokenValidityInSeconds);
    }

    public String createRefreshToken(String username) {
        return createJwtToken(username, refreshSecretKey, refreshTokenValidityInSeconds);
    }

    private String createJwtToken(String username, Key key, long validityPeriod) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + validityPeriod);
        return Jwts.builder()
                .setSubject(username)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        User principal = new User(claims.getSubject(), "N/A", new ArrayList<>());

        return new UsernamePasswordAuthenticationToken(principal, token, new ArrayList<>());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.info("Expired JWT token.");
            throw new ExpiredJwtTokenException(CommonResponseStatus.EXPIRED_JWT);
        } catch (JwtException | IllegalArgumentException e) {
            logger.info("Invalid JWT token : " + e.getMessage());
            throw new UnsuitableJwtException();
        }
    }

    public String extractIDs(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(refreshSecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new InvalidJwtTokenException(CommonResponseStatus.EXPIRED_JWT);
        } catch (JwtException e) {
            throw new InvalidJwtTokenException(CommonResponseStatus.UNSUITABLE_JWT);
        }
    }
}
