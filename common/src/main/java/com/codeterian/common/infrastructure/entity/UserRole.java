package com.codeterian.common.infrastructure.entity;

import lombok.Getter;

@Getter
public enum UserRole {

    MASTER("MASTER"),
    MANAGER("MANAGER"),
    CUSTOMER("CUSTOMER")
    ;

    // getter 메서드 추가

    private final String userRole; // final로 변경하여 불변성 보장

    UserRole(String userRole) {
        this.userRole = userRole; // 초기화
    }


}
