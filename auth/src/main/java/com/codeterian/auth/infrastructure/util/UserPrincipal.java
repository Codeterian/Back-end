package com.codeterian.auth.infrastructure.util;

import com.codeterian.auth.application.feign.dto.UserFindAllInfoResponseDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    @Getter
    private final UserFindAllInfoResponseDto userFindAllInfoResponseDto;

    public UserPrincipal(UserFindAllInfoResponseDto userFindAllInfoResponseDto) {
        this.userFindAllInfoResponseDto = userFindAllInfoResponseDto;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(userFindAllInfoResponseDto.userRole().toString()));
    }

    @Override
    public String getPassword() {
        return userFindAllInfoResponseDto.password();
    }

    @Override
    public String getUsername() {
        return userFindAllInfoResponseDto.password();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}
