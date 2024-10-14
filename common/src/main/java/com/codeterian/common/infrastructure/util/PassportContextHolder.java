package com.codeterian.common.infrastructure.util;

public class PassportContextHolder {

    private static final ThreadLocal<Passport> passportHolder = new ThreadLocal<>();

    public static void setPassport(Passport passport) {
        passportHolder.set(passport);
    }

    public static Passport getPassport() {
        return passportHolder.get();
    }

    public static void clear() {
        passportHolder.remove();
    }
}

