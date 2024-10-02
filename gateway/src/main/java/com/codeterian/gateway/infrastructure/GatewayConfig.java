package com.codeterian.gateway.infrastructure;

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
                .route("performance", r-> r.path("/performance/**").uri("lb://performance"))
                .route("ticket", r -> r.path("/ticket/**").uri("lb://ticket"))
                .route("order", r->r.path("order/**").uri("lb://order"))
                .route("payment", r-> r.path("payment/**").uri("lb://payment"))
                .route("user", r->r.path("user/**").uri("lb://user"))
                .build();
    }

}
