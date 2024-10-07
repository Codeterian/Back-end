package com.codeterian.auth.application.service;

import com.codeterian.auth.application.feign.UserFeignService;
import com.codeterian.auth.application.feign.dto.UserFindAllInfoResponseDto;
import com.codeterian.auth.presentation.LoginRequestDto;
import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.common.infrastructure.dto.UserAddRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtTokenGenerator jwtTokenGenerator;

    private final UserFeignService userFeignService;

    public String login(LoginRequestDto loginRequestDto) {
        try {
            UserFindAllInfoResponseDto userFindAllInfoResponseDto = userFeignService.getByUserEmail(loginRequestDto.email()).getData();

            // JWT 토큰 생성 및 반환
            return jwtTokenGenerator.createJwtToken(userFindAllInfoResponseDto.id(),
                    userFindAllInfoResponseDto.email(),
                    userFindAllInfoResponseDto.name(),
                    userFindAllInfoResponseDto.userRole());
        } catch (BadCredentialsException e) {
            log.error("Authentication failed for user: {}", loginRequestDto.email(), e);
            throw new RuntimeException("Invalid email or password");
        }
    }

    public ResponseDto<Void> addUser(UserAddRequestDto requestDto) {
        userFeignService.addUser(requestDto);
        return null;
    }

}
