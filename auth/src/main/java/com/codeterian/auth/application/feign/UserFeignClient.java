package com.codeterian.auth.application.feign;

import com.codeterian.auth.application.feign.dto.UserFindResponseDto;
import com.codeterian.common.infrastructure.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user")
public interface UserFeignClient {

    Logger log = LoggerFactory.getLogger(UserFeignClient.class);

    @GetMapping("/api/v1/users/{userEmail}")
    ResponseDto<UserFindResponseDto> getByUserEmail(@PathVariable("userEmail") String email);

}
