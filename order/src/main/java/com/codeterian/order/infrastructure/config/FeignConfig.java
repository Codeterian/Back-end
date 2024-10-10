package com.codeterian.order.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codeterian.common.infrastructure.util.Passport;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignConfig {

	@Bean
	public RequestInterceptor customHeaderInterceptor(){
		return new RequestInterceptor() {
			@Override
			public void apply(RequestTemplate template) {
				template.header("X-User-Id", "userId");
			}
		};
	}
}
