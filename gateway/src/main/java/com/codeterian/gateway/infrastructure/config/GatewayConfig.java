package com.codeterian.gateway.infrastructure.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoute(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("auth", r->r.path("/auth/**").uri("lb://auth"))
            // .route("auth-swagger", r -> r.path("/swagger/auth/v3/api-docs/**")
            //     .and()
            //     .method(HttpMethod.GET)
            //     .filters(f -> f.rewritePath("/swagger/auth/(?<segment>.*)", "/${segment}"))
            //     .uri("lb://auth"))


            .route("performance", r-> r.path("/api/v1/performances/**").uri("lb://performance"))
            .route("performance-swagger", r -> r.path("/swagger/performances/v3/api-docs/**")
                .and()
                .method(HttpMethod.GET)
                .filters(f -> f.rewritePath("/swagger/performances/(?<segment>.*)", "/${segment}"))
                .uri("lb://performance"))

            .route("ticket", r -> r.path("/api/v1/tickets/**").uri("lb://ticket"))
            .route("ticket-swagger", r -> r.path("/swagger/tickets/v3/api-docs/**")
                .and()
                .method(HttpMethod.GET)
                .filters(f -> f.rewritePath("/swagger/tickets/(?<segment>.*)", "/${segment}"))
                .uri("lb://ticket"))

            .route("order", r->r.path("/api/v1/orders/**").uri("lb://order"))
            .route("order-swagger", r -> r.path("/swagger/orders/v3/api-docs/**")
                .and()
                .method(HttpMethod.GET)
                .filters(f -> f.rewritePath("/swagger/orders/(?<segment>.*)", "/${segment}"))
                .uri("lb://order"))

            .route("payment", r-> r.path("/api/v1/payments/**").uri("lb://payment"))
            .route("payment-swagger", r -> r.path("/swagger/payments/v3/api-docs/**")
                .and()
                .method(HttpMethod.GET)
                .filters(f -> f.rewritePath("/swagger/payments/(?<segment>.*)", "/${segment}"))
                .uri("lb://payment"))

            .route("user", r->r.path("/api/v1/users/**").uri("lb://user"))
            .route("user-swagger", r -> r.path("/swagger/users/v3/api-docs/**")
                .and()
                .method(HttpMethod.GET)
                .filters(f -> f.rewritePath("/swagger/users/(?<segment>.*)", "/${segment}"))
                .uri("lb://user"))

            .build();
    }

}
