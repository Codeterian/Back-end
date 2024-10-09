package com.codeterian.auth.application.feign;

import com.codeterian.auth.application.feign.dto.UserFindAllInfoResponseDto;
import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.common.infrastructure.dto.UserAddRequestDto;
import org.springframework.stereotype.Service;


@Service
public interface UserFeignService {

    ResponseDto<UserFindAllInfoResponseDto> getByUserEmail(String email);

    void addUser(UserAddRequestDto requestDto);



}
