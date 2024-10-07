package com.codeterian.auth.application.feign;

import com.codeterian.auth.application.feign.dto.UserFindResponseDto;
import com.codeterian.common.infrastructure.dto.ResponseDto;

public interface UserService {

    ResponseDto<UserFindResponseDto> getByEmail(String email);

}
