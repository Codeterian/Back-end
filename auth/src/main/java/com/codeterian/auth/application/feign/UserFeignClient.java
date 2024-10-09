package com.codeterian.auth.application.feign;

import com.codeterian.auth.application.feign.dto.UserFindAllInfoResponseDto;
import com.codeterian.auth.application.feign.fallback.UserServiceFallback;
import com.codeterian.auth.infrastructure.config.FeignClientConfig;
import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.common.infrastructure.dto.UserAddRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user",
        configuration = FeignClientConfig.class,
        fallback = UserServiceFallback.class)
public interface UserFeignClient extends UserFeignService {

    @GetMapping("/api/v1/users/email/{userEmail}")
    ResponseDto<UserFindAllInfoResponseDto> getByUserEmail(@PathVariable("userEmail") String email);

    @PostMapping("/api/v1/users")
    ResponseDto<Void> addUser(@RequestBody UserAddRequestDto requestDto);

}