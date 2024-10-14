package com.codeterian.ticket.application.feign;

import com.codeterian.common.infrastructure.dto.ResponseDto;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
public class UserFallBack implements UserFeignClient{

    private final Throwable cause;

    public UserFallBack(Throwable cause) {
        this.cause = cause;
    }


    @Override
    public ResponseEntity<ResponseDto<UserFindResponseDto>> findById(Long userId) {

        if(cause instanceof FeignException.NotFound) {
            log.error("error!");
        }
        else {
            log.error("fallback error!");
        }

        return null;
    }

}
