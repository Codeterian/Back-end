package com.codeterian.user.presentation.controller;

import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.common.infrastructure.dto.UserAddRequestDto;
import com.codeterian.user.application.service.UserService;
import com.codeterian.user.presentation.dto.UserLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserPublicController {

    private final UserService userService;

    @PostMapping("/login")
    public String userLogin(@RequestBody UserLoginRequestDto requestDto) {
        userService.loginUser(requestDto);
        return "token";
    }


    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto<Void>> userAdd(@RequestBody UserAddRequestDto requestDto) {
        userService.addUser(requestDto);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<Void>> userLogout(@RequestHeader("Authorization") String token) {
        userService.logoutUser(token);
        return ResponseEntity.ok(ResponseDto.ok());
    }

}
