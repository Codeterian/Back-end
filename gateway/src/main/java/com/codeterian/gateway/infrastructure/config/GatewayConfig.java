package com.codeterian.gateway.infrastructure.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth", r->r.path("/auth/**").uri("lb://auth"))
                .route("performance", r-> r.path("/api/v1/performances/**").uri("lb://performance"))
                .route("ticket", r -> r.path("/api/v1/tickets/**").uri("lb://ticket"))
                .route("order", r->r.path("/api/v1/orders/**").uri("lb://order"))
                .route("payment", r-> r.path("/api/v1/payments/**").uri("lb://payment"))
                .route("user", r->r.path("/api/v1/users/**").uri("lb://user"))
                .build();
    }

}
