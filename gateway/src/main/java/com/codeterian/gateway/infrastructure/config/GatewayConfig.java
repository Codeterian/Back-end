package com.codeterian.gateway.infrastructure.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.socket.client.TomcatWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.reactive.socket.server.RequestUpgradeStrategy;
import org.springframework.web.reactive.socket.server.upgrade.TomcatRequestUpgradeStrategy;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoute(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("queue", r->r.path("/api/v1/queues/**", "/ws-stomp")
                        .uri("lb://queue"))
            .route("queue-swagger", r -> r.path("/swagger/queues/v3/api-docs/**")
                        .and()
                        .method(HttpMethod.GET)
                        .filters(f -> f.rewritePath("/swagger/queues/(?<segment>.*)", "/${segment}"))
                        .uri("lb://queue"))

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

            .route("user", r->r.path("/api/v1/users/**", "/login", "/sign-up", "/logout").uri("lb://user"))
            .route("user-swagger", r -> r.path("/swagger/users/v3/api-docs/**")
                .and()
                .method(HttpMethod.GET)
                .filters(f -> f.rewritePath("/swagger/users/(?<segment>.*)", "/${segment}"))
                .uri("lb://user"))
            .build();
    }

    @Bean
    @Primary
    public WebSocketClient tomcatWebSocketClient() {
        return new TomcatWebSocketClient();
    }

    @Bean
    @Primary
    public RequestUpgradeStrategy requestUpgradeStrategy() {
        return new TomcatRequestUpgradeStrategy();
    }


}
