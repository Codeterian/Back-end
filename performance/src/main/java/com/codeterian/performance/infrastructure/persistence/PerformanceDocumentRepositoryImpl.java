package com.codeterian.performance.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.codeterian.performance.domain.performance.PerformanceDocument;
import com.codeterian.performance.domain.repository.PerformanceDocumentRepository;

@Repository
public interface PerformanceDocumentRepositoryImpl extends ElasticsearchRepository<PerformanceDocument, UUID> ,
	PerformanceDocumentRepository {

}
