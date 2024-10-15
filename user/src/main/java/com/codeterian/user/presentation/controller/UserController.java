package com.codeterian.user.presentation.controller;


import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.common.infrastructure.util.CurrentPassport;
import com.codeterian.common.infrastructure.util.Passport;
import com.codeterian.user.application.service.UserService;
import com.codeterian.user.presentation.dto.request.UserModifyRequestDto;
import com.codeterian.user.presentation.dto.response.UserFindAllInfoResponseDto;
import com.codeterian.user.presentation.dto.response.UserFindResponseDto;
import com.codeterian.user.presentation.dto.response.UserModifyResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirement(name = "Bearer Authentication")
@SecurityScheme( name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "Bearer")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "유저 API")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "403", description = "권한 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
})
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @Operation(summary = "유저 단건 조회 by ID", description = "유저 단건 조회 by ID API")
    public ResponseEntity<ResponseDto<UserFindResponseDto>> userDetails(@PathVariable("userId") Long userId,
                                                                        @Parameter(hidden = true) @CurrentPassport Passport passport) throws IllegalAccessException {
        return ResponseEntity.ok(ResponseDto.okWithData(userService.findUserById(userId, passport)));
    }

    @GetMapping("/email/{userEmail}")
    @Operation(summary = "유저 단건 조회 by 이메일", description = "유저 단건 조회 by 이메일 API")
    public ResponseDto<UserFindAllInfoResponseDto> userDetailsByEmail(@PathVariable("userEmail") String email) {
        return ResponseDto.okWithData(userService.findByEmail(email));
    }

    @GetMapping
    @Operation(summary = "유저 전체 조회", description = "유저 전체 조회 API")
    public ResponseEntity<ResponseDto<List<UserFindResponseDto>>> userList(
            @Parameter(hidden = true) @CurrentPassport Passport passport) throws IllegalAccessException {
        return ResponseEntity.ok(ResponseDto.okWithData(userService.findAllUser(passport)));
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "유저 정보 수정", description = "유저 정보 수정 API")
    public ResponseEntity<ResponseDto<UserModifyResponseDto>> userModify(@PathVariable("userId") Long userId,
                                                                         @RequestBody UserModifyRequestDto requestDto,
                                                                         @Parameter(hidden = true) @CurrentPassport Passport passport) throws IllegalAccessException {
        return ResponseEntity.ok(ResponseDto.okWithData(userService.modifyUser(userId, requestDto, passport)));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "유저 삭제", description = "유저 삭제 API")
    public ResponseEntity<ResponseDto<Void>> userDelete(@PathVariable("userId") Long userId,
                                                        @Parameter(hidden = true) @CurrentPassport Passport passport) throws IllegalAccessException {

        userService.deleteUser(userId, passport);

        return ResponseEntity.ok(ResponseDto.ok());
    }


}
