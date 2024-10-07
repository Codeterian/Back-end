package com.codeterian.order.infrastructure.config;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.addServersItem(new Server().url("/"))
			.info(new Info()
				.title("Order Service API")
				.version("v3")
				.description("API Documentation for Order Service"));
	}

	// 해당 도메인은 모든 API에 권한 인증이 필요
	@Bean
	public OperationCustomizer globalHeadersCustomizer() {
		return (operation, handlerMethod) -> {
			// X-User-Name 헤더 추가
			operation.addParametersItem(new Parameter()
				.in("header")
				.name("X-User-Name")
				.description("권한 확인을 위한 username")
				.required(true)
				.schema(new StringSchema()));

			// X-User-Role 헤더 추가
			operation.addParametersItem(new Parameter()
				.in("header")
				.name("X-User-Role")
				.description("권한 확인을 위한 Authority")
				.required(true)
				.schema(new StringSchema()));

			return operation;
		};
	}
}
