package com.codeterian.auth.application.feign.fallback;

import com.codeterian.auth.application.feign.UserFeignClient;
import com.codeterian.auth.application.feign.dto.UserFindAllInfoResponseDto;
import com.codeterian.common.infrastructure.dto.ResponseDto;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserServiceFallback implements UserFeignClient {

    private final Throwable cause;

    public UserServiceFallback(Throwable cause) {
        this.cause = cause;
    }


    @Override
    public ResponseDto<UserFindAllInfoResponseDto> getByUserEmail(String email) {
        if (cause instanceof FeignException.NotFound) {
            log.error("Feign getByUserEmail error!!");
        } else {
            log.error("fallback error");
        }
        return  null;
    }

}
