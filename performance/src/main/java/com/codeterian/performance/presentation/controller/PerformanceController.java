package com.codeterian.performance.presentation.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeterian.performance.application.PerformanceService;
import com.codeterian.performance.presentation.dto.request.PerformanceAddRequestDto;
import com.codeterian.performance.presentation.dto.request.PerformanceModifyRequestDto;
import com.codeterian.performance.presentation.dto.response.PerformanceDetailsResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/performances")
public class PerformanceController {

    private final PerformanceService performanceService;

    @PostMapping
    public ResponseEntity<?> performanceAdd(@RequestBody PerformanceAddRequestDto dto){
        performanceService.addPerformance(dto);
        return ResponseEntity.ok().body("공연 등록에 성공했습니다.");
    }

    @PutMapping("/{performanceId}")
    public ResponseEntity<?> performanceModify(@PathVariable UUID performanceId, @RequestBody PerformanceModifyRequestDto dto){
        performanceService.modifyPerformance(performanceId,dto);
        return ResponseEntity.ok().body("공연 수정에 성공했습니다.");
    }

    @GetMapping("/{performanceId}")
    public ResponseEntity<PerformanceDetailsResponseDto> performanceDetails(@PathVariable UUID performanceId) {
        return ResponseEntity.ok(performanceService.findPerformanceDetails(performanceId));
    }

    @GetMapping("/search")
    public ResponseEntity<?> performanceSearch(@RequestParam String query, @RequestParam int pageNumber, @RequestParam int pageSize) {
        return ResponseEntity.ok(performanceService.searchPerformance(query,pageNumber,pageSize));
    }
}
