package com.codeterian.auth.infrastructure.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Value("${server.port}")
    private String serverPort;

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.HEADERS;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header("server-port", serverPort);
    }

}
