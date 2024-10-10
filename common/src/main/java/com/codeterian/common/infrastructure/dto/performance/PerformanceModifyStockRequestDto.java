package com.codeterian.common.infrastructure.dto.performance;

import java.util.UUID;

public record PerformanceModifyStockRequestDto(
	UUID performanceId,
	Integer number
) {
}
