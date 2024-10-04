package com.codeterian.user.presentation.controller;


import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.user.application.service.UserService;
import com.codeterian.user.presentation.dto.request.UserAddRequestDto;
import com.codeterian.user.presentation.dto.request.UserModifyRequestDto;
import com.codeterian.user.presentation.dto.response.UserFindResponseDto;
import com.codeterian.user.presentation.dto.response.UserModifyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ResponseDto<Void>> userAdd(@RequestBody UserAddRequestDto requestDto) {
        userService.addUser(requestDto);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserFindResponseDto>> userDetails(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(ResponseDto.okWithData(userService.findUserById(userId)));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<UserFindResponseDto>>> userList() {
        return ResponseEntity.ok(ResponseDto.okWithData(userService.findAllUser()));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserModifyResponseDto>> userModify(@PathVariable("userId") Long userId,
                                                                         @RequestBody UserModifyRequestDto requestDto) {
        return ResponseEntity.ok(ResponseDto.okWithData(userService.modifyUser(userId, requestDto)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDto<Void>> userDelete(@PathVariable("userId") Long userId) {

        userService.deleteUser(userId);

        return ResponseEntity.ok(ResponseDto.ok());
    }


}
