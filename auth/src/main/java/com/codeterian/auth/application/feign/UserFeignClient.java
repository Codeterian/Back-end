package com.codeterian.auth.application.feign;

import com.codeterian.auth.application.feign.dto.UserFindAllInfoResponseDto;
import com.codeterian.auth.application.feign.fallback.UserServiceFallback;
import com.codeterian.auth.infrastructure.config.FeignClientConfig;
import com.codeterian.common.infrastructure.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user",
configuration = FeignClientConfig.class,
fallback = UserServiceFallback.class)
public interface UserFeignClient extends UserFeignService{

    Logger log = LoggerFactory.getLogger(UserFeignClient.class);

    @GetMapping("/api/v1/users/email/{userEmail}")
    ResponseDto<UserFindAllInfoResponseDto> getByUserEmail(@PathVariable("userEmail") String email);

}
