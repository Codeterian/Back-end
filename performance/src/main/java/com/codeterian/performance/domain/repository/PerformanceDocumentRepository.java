package com.codeterian.performance.domain.repository;

import com.codeterian.performance.domain.performance.PerformanceDocument;

public interface PerformanceDocumentRepository {

	PerformanceDocument save(PerformanceDocument performanceDocument);
}
