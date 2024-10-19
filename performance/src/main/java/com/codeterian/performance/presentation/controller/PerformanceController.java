package com.codeterian.performance.presentation.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codeterian.common.infrastructure.util.CurrentPassport;
import com.codeterian.common.infrastructure.util.Passport;
import com.codeterian.performance.application.PerformanceService;
import com.codeterian.performance.presentation.dto.request.PerformanceAddRequestDto;
import com.codeterian.performance.presentation.dto.request.PerformanceModifyRequestDto;
import com.codeterian.performance.presentation.dto.response.ParentCategoryAddResponseDto;
import com.codeterian.performance.presentation.dto.response.PerformanceAddResponseDto;
import com.codeterian.performance.presentation.dto.response.PerformanceDetailsResponseDto;
import com.codeterian.performance.presentation.dto.response.PerformanceModifyResponseDto;
import com.codeterian.performance.presentation.dto.response.PerformanceSearchResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@SecurityRequirement(name = "Bearer Authentication")
@SecurityScheme( name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "Bearer")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/performances")
@Tag(name = "공연 API")
public class PerformanceController {

    private final PerformanceService performanceService;

    @Operation(summary = "공연 등록" ,description = "공연 등록 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200" , description = "공연 등록 성공",
            content = {@Content(schema = @Schema(implementation = PerformanceAddResponseDto.class))}),
        @ApiResponse(responseCode = "403" ,description = "관리자 회원이 아닙니다."),
        @ApiResponse(responseCode = "404" ,description = "존재하지 않는 카테고리입니다."),
        @ApiResponse(responseCode = "409",description = "중복되는 공연입니다."),
        @ApiResponse(responseCode = "500",description = "이미지 업로드에 실패했습니다.")
    })
    @PostMapping
    public ResponseEntity<PerformanceAddResponseDto> performanceAdd(
        @RequestPart PerformanceAddRequestDto dto,
        @RequestPart("titleImage") MultipartFile titleImage,
        @RequestPart("images")List<MultipartFile> images,
        @Parameter(hidden = true)@CurrentPassport Passport passport) throws IOException {
        return ResponseEntity.ok().body( performanceService.addPerformance(dto,titleImage,images,passport));
    }

    @Operation(summary = "공연 수정" ,description = "공연 수정 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200" , description = "공연 수정 성공",
            content = {@Content(schema = @Schema(implementation = PerformanceModifyResponseDto.class))}),
        @ApiResponse(responseCode = "403" ,description = "관리자 회원이 아닙니다."),
        @ApiResponse(responseCode = "404" ,description = "존재하지 않는 카테고리입니다."),
        @ApiResponse(responseCode = "404",description = "존재하지 않는 공연입니다."),
        @ApiResponse(responseCode = "500",description = "이미지 업로드에 실패했습니다.")
    })
    @PutMapping("/{performanceId}")
    public ResponseEntity<PerformanceModifyResponseDto> performanceModify(
        @PathVariable UUID performanceId,
        @RequestPart PerformanceModifyRequestDto dto,
        @RequestPart(value = "titleImage",required = false) MultipartFile titleImage,
        @RequestPart(value = "images",required = false)List<MultipartFile> images,
        @Parameter(hidden = true)@CurrentPassport Passport passport) throws IOException {
        return ResponseEntity.ok()
            .body(performanceService.modifyPerformance(performanceId,dto,titleImage,images,passport));
    }

    @Operation(summary = "공연 상세 조회" ,description = "공연 상세 조회 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200" , description = "공연 상세 조회 성공",
            content = {@Content(schema = @Schema(implementation = PerformanceDetailsResponseDto.class))}),
        @ApiResponse(responseCode = "404",description = "존재하지 않는 공연입니다.")
    })
    @GetMapping("/{performanceId}")
    public ResponseEntity<PerformanceDetailsResponseDto> performanceDetails(@PathVariable UUID performanceId) {
        return ResponseEntity.ok(performanceService.findPerformanceDetails(performanceId));
    }

    @Operation(summary = "공연 검색" ,description = "공연 검색 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200" , description = "공연 검색 성공",
            content = {@Content(schema = @Schema(implementation = PerformanceSearchResponseDto.class))}),
        @ApiResponse(responseCode = "400",description = "잘못된 pageNumber 요청입니다."),
        @ApiResponse(responseCode = "400",description = "잘못된 잘못된 pageSize 요청입니다.")
    })
    @GetMapping("/search")
    public ResponseEntity<List<PerformanceSearchResponseDto>> performanceSearch(
        @RequestParam String query,
        @RequestParam (required = false,defaultValue = "0")int pageNumber,
        @RequestParam (required = false,defaultValue = "10")int pageSize) {

        return ResponseEntity.ok(performanceService.searchPerformance(query,pageNumber,pageSize));
    }

    @Operation(summary = "공연 삭제" ,description = "공연 삭제 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200" , description = "공연 삭제 성공"),
        @ApiResponse(responseCode = "404",description = "존재하지 않는 공연입니다.")
    })
    @DeleteMapping("/{performanceId}")
    public ResponseEntity<?> performanceRemove(
        @PathVariable UUID performanceId,
        @Parameter(hidden = true)@CurrentPassport Passport passport) {
        performanceService.removePerformance(performanceId,passport);
        return ResponseEntity.ok().body("공연 삭제에 성공했습니다.");
    }
}
