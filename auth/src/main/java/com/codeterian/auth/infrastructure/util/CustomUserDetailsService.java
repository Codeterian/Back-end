package com.codeterian.auth.infrastructure.util;

import com.codeterian.auth.application.feign.UserFeignService;
import com.codeterian.auth.application.feign.dto.UserFindAllInfoResponseDto;
import com.codeterian.common.infrastructure.dto.ResponseDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserFeignService userService;

    public CustomUserDetailsService(UserFeignService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ResponseDto<UserFindAllInfoResponseDto> response = userService.getByUserEmail(username);
        UserFindAllInfoResponseDto userFindAllInfoResponseDto = response.getData();

        if (userFindAllInfoResponseDto == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        return new UserPrincipal(userFindAllInfoResponseDto);
    }

}
