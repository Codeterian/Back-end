package com.codeterian.order.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.codeterian.common.infrastructure.dto.performance.PerformanceModifyStockRequestDto;
import com.codeterian.order.infrastructure.config.FeignOkHttpConfig;

@FeignClient(name = "performance", url = "http://localhost:19093", path = "/api/v1/performances", configuration = FeignOkHttpConfig.class)
public interface PerformanceClient {
	@PatchMapping("/modifyStock")
	void modifyStockPerformanceFromOrders(
		@RequestBody PerformanceModifyStockRequestDto performanceModifyStockRequestDto);

}
