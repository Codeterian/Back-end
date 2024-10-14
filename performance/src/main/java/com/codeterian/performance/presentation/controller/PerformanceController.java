package com.codeterian.performance.presentation.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codeterian.performance.application.PerformanceService;
import com.codeterian.performance.presentation.dto.request.PerformanceAddRequestDto;
import com.codeterian.performance.presentation.dto.request.PerformanceModifyRequestDto;
import com.codeterian.performance.presentation.dto.response.PerformanceAddResponseDto;
import com.codeterian.performance.presentation.dto.response.PerformanceDetailsResponseDto;
import com.codeterian.performance.presentation.dto.response.PerformanceModifyResponseDto;
import com.codeterian.performance.presentation.dto.response.PerformanceSearchResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/performances")
public class PerformanceController {

    private final PerformanceService performanceService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PerformanceAddResponseDto> performanceAdd(
        @RequestPart PerformanceAddRequestDto dto,
        @RequestPart("titleImage") MultipartFile titleImage,
        @RequestPart("images") List<MultipartFile> images
    ) throws IOException {
        return ResponseEntity.ok().body( performanceService.addPerformance(dto,titleImage,images));
    }

    @PutMapping("/{performanceId}")
    public ResponseEntity<PerformanceModifyResponseDto> performanceModify(@PathVariable UUID performanceId, @RequestBody PerformanceModifyRequestDto dto){
        return ResponseEntity.ok().body(performanceService.modifyPerformance(performanceId,dto));
    }

    @GetMapping("/{performanceId}")
    public ResponseEntity<PerformanceDetailsResponseDto> performanceDetails(@PathVariable UUID performanceId) {
        return ResponseEntity.ok(performanceService.findPerformanceDetails(performanceId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PerformanceSearchResponseDto>> performanceSearch(
        @RequestParam String query,
        @RequestParam (required = false,defaultValue = "0")int pageNumber,
        @RequestParam (required = false,defaultValue = "10")int pageSize) {

        return ResponseEntity.ok(performanceService.searchPerformance(query,pageNumber,pageSize));
    }

    @DeleteMapping("/{performanceId}")
    public ResponseEntity<?> performanceRemove(@PathVariable UUID performanceId) {
        performanceService.removePerformance(performanceId);
        return ResponseEntity.ok().body("공연 삭제에 성공했습니다.");
    }
}
