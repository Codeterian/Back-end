package com.codeterian.user.presentation.controller;

import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.common.infrastructure.dto.UserAddRequestDto;
import com.codeterian.user.application.service.UserService;
import com.codeterian.user.presentation.dto.UserLoginRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "유저 Public API")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "403", description = "권한 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
})
public class UserPublicController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "로그인",
            description = "로그인 API 입니다. 다른 기능 사용 시 로그인 요청 시 반환되는 값을 그대로 복사하여 헤더에 Authorization을 key로 등록하면 됩니다.")
    public String userLogin(@RequestBody UserLoginRequestDto requestDto) {
        return "Bearer "+userService.loginUser(requestDto);
    }


    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "회원가입 API")
    public ResponseEntity<ResponseDto<Void>> userAdd(@Valid @RequestBody UserAddRequestDto requestDto) {
        userService.addUser(requestDto);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃 API")
    public ResponseEntity<ResponseDto<Void>> userLogout(@RequestHeader("Authorization") String token) {
        userService.logoutUser(token);
        return ResponseEntity.ok(ResponseDto.ok());
    }

}
