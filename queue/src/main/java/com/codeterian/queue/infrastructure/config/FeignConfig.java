package com.codeterian.queue.infrastructure.config;

import com.codeterian.common.infrastructure.util.Passport;
import com.codeterian.common.infrastructure.util.PassportContextHolder;
import com.codeterian.common.infrastructure.util.TokenUtils;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FeignConfig {

    private final TokenUtils tokenUtils;

    private final String internalSecretKey;

    public FeignConfig(TokenUtils tokenUtils, @Value("${jwt.secret.internal-secret-key}") String internalSecretKey) {
        this.tokenUtils = tokenUtils;
        this.internalSecretKey = internalSecretKey;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Passport passport = PassportContextHolder.getPassport();

            if (passport != null) {
                // Passport 객체를 JWT로 변환
                String token = tokenUtils.passportToToken(passport, internalSecretKey);
                // JWT를 헤더에 추가
                requestTemplate.header("InternalToken", "Bearer " + token);
                log.info("InternalToken: " + token);

            }

        };
    }

}
