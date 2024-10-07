package com.codeterian.common.infrastructure.util;

import com.codeterian.common.infrastructure.entity.UserRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Passport {

    private Integer userId;

    private String email;

    private String userName;

    private Date expirationTime;
    private UserRole userRole;

    public Passport(Integer userId, String userName, String email, Date expirationTime,
                    UserRole userRole) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.expirationTime = expirationTime;
        this.userRole = userRole;
    }

}
