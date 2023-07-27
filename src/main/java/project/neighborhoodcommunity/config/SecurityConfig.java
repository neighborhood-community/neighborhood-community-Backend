package project.neighborhoodcommunity.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import project.neighborhoodcommunity.jwt.JwtAuthenticationEntryPoint;
import project.neighborhoodcommunity.jwt.JwtSecurityConfig;
import project.neighborhoodcommunity.jwt.JwtTokenProvider;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()

                .headers()
                .frameOptions()
                .sameOrigin()
                .and()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .authorizeHttpRequests()
                .requestMatchers("/jwt").authenticated()
                .anyRequest().permitAll()
                .and()

//                .requiresChannel()
//                .anyRequest().requiresSecure()
//                .and()

                .apply(new JwtSecurityConfig(jwtTokenProvider));
        return http.build();
    }
}
