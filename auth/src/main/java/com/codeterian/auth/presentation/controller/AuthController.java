package com.codeterian.auth.presentation.controller;

import com.codeterian.auth.application.service.AuthService;
import com.codeterian.auth.presentation.LoginRequestDto;
import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.common.infrastructure.dto.UserAddRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseDto<Void> addUser(@RequestBody UserAddRequestDto requestDto) { return authService.addUser(requestDto);}

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto requestDto) {
        return authService.login(requestDto);
    }



}
