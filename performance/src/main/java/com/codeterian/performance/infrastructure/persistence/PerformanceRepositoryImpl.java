package com.codeterian.performance.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codeterian.performance.domain.performance.Performance;
import com.codeterian.performance.domain.repository.PerformanceRepository;

@Repository
public interface PerformanceRepositoryImpl extends JpaRepository<Performance,UUID> ,PerformanceRepository {

}