package com.codeterian.user.presentation.controller;


import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.common.infrastructure.util.CurrentPassport;
import com.codeterian.common.infrastructure.util.Passport;
import com.codeterian.user.application.service.UserService;
import com.codeterian.user.presentation.dto.request.UserModifyRequestDto;
import com.codeterian.user.presentation.dto.response.UserFindAllInfoResponseDto;
import com.codeterian.user.presentation.dto.response.UserFindResponseDto;
import com.codeterian.user.presentation.dto.response.UserModifyResponseDto;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserFindResponseDto>> userDetails(@PathVariable("userId") Long userId,
                                                                        @CurrentPassport Passport passport) throws IllegalAccessException {
        return ResponseEntity.ok(ResponseDto.okWithData(userService.findUserById(userId, passport)));
    }

    @GetMapping("/email/{userEmail}")
    public ResponseDto<UserFindAllInfoResponseDto> userDetailsByEmail(@PathVariable("userEmail") String email) {
        return ResponseDto.okWithData(userService.findByEmail(email));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<UserFindResponseDto>>> userList(@CurrentPassport Passport passport) throws IllegalAccessException {
        return ResponseEntity.ok(ResponseDto.okWithData(userService.findAllUser(passport)));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserModifyResponseDto>> userModify(@PathVariable("userId") Long userId,
                                                                         @RequestBody UserModifyRequestDto requestDto,
                                                                         @CurrentPassport Passport passport) throws IllegalAccessException {
        return ResponseEntity.ok(ResponseDto.okWithData(userService.modifyUser(userId, requestDto, passport)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDto<Void>> userDelete(@PathVariable("userId") Long userId,
                                                        @CurrentPassport Passport passport) throws IllegalAccessException {

        userService.deleteUser(userId, passport);

        return ResponseEntity.ok(ResponseDto.ok());
    }


}
