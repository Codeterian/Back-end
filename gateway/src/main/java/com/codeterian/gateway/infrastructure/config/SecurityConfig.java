package com.codeterian.gateway.infrastructure.config;

import com.codeterian.common.infrastructure.dto.UserDto;
import com.codeterian.gateway.application.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Optional;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKeyString;

    private final RedisService redisService;

    public SecurityConfig(RedisService redisService) {
        this.redisService = redisService;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/auth/login", "/auth/sign-up").permitAll()  // 로그인 경로는 허용
                        .anyExchange().authenticated()  // 나머지 요청은 인증 필요
                )
                .addFilterAt(jwtAuthenticationFilter(redisService), SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }

    @Bean
    public WebFilter jwtAuthenticationFilter(RedisService redisService) {

        return (exchange, chain) -> {
            // /auth/login 경로는 필터를 적용하지 않음
            if (exchange.getRequest().getURI().getPath().startsWith("/auth/login")&&
                exchange.getRequest().getURI().getPath().startsWith("/auth/sign-up")) {
                return chain.filter(exchange);
            }

            HttpHeaders headers = exchange.getRequest().getHeaders();
            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    byte[] bytes = Base64.getDecoder().decode(secretKeyString);
                    SecretKey secretKey = Keys.hmacShaKeyFor(bytes);

                    Claims claims = Jwts
                            .parserBuilder()
                            .setSigningKey(secretKey).build()
                            .parseClaimsJws(token)
                            .getBody();

                    String username = claims.getSubject();

                    var userDto =
                            Optional.ofNullable(
                                            redisService.getValueAsClass("user:" + username, UserDto.class)
                                    )
                                    .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found")
                                    );

                    // 사용자 정보를 새로운 헤더에 추가
                    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                            .header("X-User-Name", username)  // 사용자명 헤더 추가
                            .header("X-User-Roles", String.join(",", userDto.roles()))    // 권한 정보 헤더 추가
                            .build();

                    // 수정된 요청으로 필터 체인 계속 처리
                    ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();
                    return chain.filter(modifiedExchange);

                } catch (Exception e) {
                    return Mono.error(new RuntimeException("Invalid JWT Token"));
                }
            }

            return chain.filter(exchange);
        };
    }

}
