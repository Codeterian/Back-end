package com.codeterian.ticket.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.addServersItem(new Server().url("/"))
			.info(new Info()
				.title("Ticket Service API")
				.version("v3")
				.description("API Documentation for Ticket Service"));
	}

}

