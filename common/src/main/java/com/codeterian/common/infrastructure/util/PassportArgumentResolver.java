package com.codeterian.common.infrastructure.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
public class PassportArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenUtils tokenUtils;
    private final String internalSecretKey;

    public PassportArgumentResolver(TokenUtils tokenUtils, @Value("${jwt.secret.internal-secret-key}") String internalSecretKey) {
        this.tokenUtils = tokenUtils;
        this.internalSecretKey = internalSecretKey;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentPassport.class) && parameter.getParameterType().equals(Passport.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception
    {
        String passportToken = extractToken(webRequest);

        return tokenUtils.toPassport(passportToken, internalSecretKey);
    }

    private String extractToken(NativeWebRequest request) {
        String authHeader = request.getHeader("InternalToken");
        log.info("resolver external Token");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

}
