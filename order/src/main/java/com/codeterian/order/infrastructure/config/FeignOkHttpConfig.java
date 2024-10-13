package com.codeterian.order.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;

@Configuration
public class FeignOkHttpConfig {
	@Bean
	public okhttp3.OkHttpClient okHttpClient() {
		return new okhttp3.OkHttpClient.Builder().build();
	}

	@Bean
	public feign.Client feignClient() {
		return new feign.okhttp.OkHttpClient(okHttpClient());
	}

	// Feign 로깅 레벨 설정 (전체 요청과 응답 로깅)
	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
}
