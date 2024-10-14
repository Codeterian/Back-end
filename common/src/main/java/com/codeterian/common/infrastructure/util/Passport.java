package com.codeterian.common.infrastructure.util;

import com.codeterian.common.infrastructure.entity.UserRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Passport {

    private UUID id;

    private Long userId;

    private String email;

    private String userName;

    private Date expirationTime;

    private UserRole userRole;

    public Passport(Long userId, String userName, String email, Date expirationTime,
                    UserRole userRole) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.expirationTime = expirationTime;
        this.userRole = userRole;
    }

}
