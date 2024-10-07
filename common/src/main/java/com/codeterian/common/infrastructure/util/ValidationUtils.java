package com.codeterian.common.infrastructure.util;

import com.codeterian.common.infrastructure.entity.UserRole;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class ValidationUtils {

    /**
     *
     * @param roles : 호출 가능한 사용자 타입
     * @param userType : 호출자 사용자 타입
     */
    public static void validateRole(Stream<UserRole> roles, UserRole userType) throws IllegalAccessException {
        if (!roles.anyMatch(i -> i == userType)) {
            throw new IllegalAccessException();
        }
    }

}
