package com.codeterian.common.infrastructure.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Component
@Slf4j
public class CustomAuditorAware implements AuditorAware<Long> {


    private final TokenUtils tokenUtils;
    private final String internalSecretKey;

    public CustomAuditorAware(TokenUtils tokenUtils,@Value("${jwt.secret.internal-secret-key}") String internalSecretKey) {
        this.tokenUtils = tokenUtils;
        this.internalSecretKey = internalSecretKey;
    }



    @Override
    public Optional<Long> getCurrentAuditor() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String passportToken = extractToken(request);
        log.info(passportToken);
        if (passportToken != null) {
            Passport passPort = tokenUtils.toPassport(passportToken, internalSecretKey);
            return Optional.of(passPort.getUserId());
        }
        return Optional.empty();
    }


    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("InternalToken");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
