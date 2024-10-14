package com.codeterian.ticket.application.feign;

import com.codeterian.common.infrastructure.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    ResponseEntity<ResponseDto<UserFindResponseDto>> findById(Long userId);

}
