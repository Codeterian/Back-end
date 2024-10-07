package com.codeterian.common.infrastructure.util;

import com.codeterian.common.infrastructure.entity.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PassportRoleCheck {
    UserRole role();
}
