package com.codeterian.ticket.application.feign;

import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.ticket.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="user",
fallbackFactory = UserFallBackFactory.class,
configuration = FeignConfig.class)
public interface UserFeignClient extends UserService  {

    @Override
    @GetMapping("/api/v1/users/{userId}")
    ResponseEntity<ResponseDto<UserFindResponseDto>> findById(@PathVariable Long userId);

}
