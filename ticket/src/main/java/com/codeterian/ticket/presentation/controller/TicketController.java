package com.codeterian.ticket.presentation.controller;

import java.util.List;
import java.util.UUID;

import com.codeterian.common.infrastructure.entity.UserRole;
import com.codeterian.common.infrastructure.util.CurrentPassport;
import com.codeterian.common.infrastructure.util.Passport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.common.infrastructure.dto.ticket.TicketAddRequestDto;
import com.codeterian.ticket.application.service.TicketPerformanceService;
import com.codeterian.ticket.application.service.TicketService;
import com.codeterian.ticket.presentation.dto.request.TicketModifyRequestDto;
import com.codeterian.ticket.presentation.dto.response.TicketFindResponseDto;
import com.codeterian.ticket.presentation.dto.response.TicketModifyResponseDto;

import lombok.RequiredArgsConstructor;

@SecurityRequirement(name = "Bearer Authentication")
@SecurityScheme( name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "Bearer")
@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "티켓 API")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "403", description = "권한 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
})
public class TicketController {

    private final TicketService ticketService;

    private final TicketPerformanceService ticketOrdersService;

    @PostMapping
    @Operation(summary = "티켓 추가 테스트", description = "티켓 추가 테스트 API")
    public ResponseEntity<ResponseDto<Void>> ticketAdd(@RequestBody TicketAddRequestDto requestDto,
                                                       @Parameter(hidden = true) @CurrentPassport Passport passport) {
        ticketService.addTicket(requestDto, passport.getUserId(), passport);

        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/{ticketId}")
    @Operation(summary = "티켓 단건 조회", description = "티켓 단건 조회 API")
    public ResponseEntity<ResponseDto<TicketFindResponseDto>> ticketFind(
            @PathVariable("ticketId")UUID ticketId
    ) {
      return ResponseEntity.ok(ResponseDto.okWithData(ticketService.findTicketById(ticketId)));
    }

    @GetMapping
    @Operation(summary = "티켓 전체 조회", description = "티켓 전체 조회 API")
    public ResponseEntity<ResponseDto<List<TicketFindResponseDto>>> ticketList(
            @CurrentPassport Passport passport
    ) throws IllegalAccessException {

        if (passport.getUserRole()== UserRole.CUSTOMER) {
            throw new IllegalAccessException("접근 불가");
        }

        return ResponseEntity.ok(ResponseDto.okWithData(ticketService.findAllTicket()));
    }

    @PatchMapping("/{ticketId}")
    @Operation(summary = "티켓 수정", description = "티켓 수정 API")
    public ResponseEntity<ResponseDto<TicketModifyResponseDto>> ticketModify(
            @PathVariable("ticketId") UUID ticketId,
            @RequestBody TicketModifyRequestDto requestDto,
            @Parameter(hidden = true) @CurrentPassport Passport passport
    ) throws IllegalAccessException {
        if(passport.getUserRole()==UserRole.CUSTOMER) {
            throw new IllegalAccessException();
        }

        return ResponseEntity.ok(ResponseDto.okWithData(ticketService.modifyTicket(ticketId, requestDto)));
    }

    @DeleteMapping("/{ticketId}")
    @Operation(summary = "티켓 삭제", description = "티켓 삭제 API")
    public ResponseEntity<ResponseDto<Void>> ticketDelete(
            @PathVariable("ticketId") UUID ticketId
    ) {
        ticketService.deleteTicketById(ticketId);

        return ResponseEntity.ok(ResponseDto.ok());
    }
}
