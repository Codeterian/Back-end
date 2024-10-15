package com.codeterian.order.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		String jwtSchemeName = "Authorization";
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
		Components components = new Components()
			.addSecuritySchemes(jwtSchemeName, new SecurityScheme()
				.name(jwtSchemeName)
				.type(SecurityScheme.Type.HTTP)
				.scheme("Bearer")
				.bearerFormat("JWT"));
		return new OpenAPI()
			.addServersItem(new Server().url("/"))
			.info(new Info()
				.title("Order Service API")
				.version("v3")
				.description("API Documentation for Order Service"))
			.components(components);
	}
}
