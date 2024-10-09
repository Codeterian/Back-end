package com.codeterian.ticket.infrastructure.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    //락의 이름
    String key();

    //락의 시간 단위
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    long waitTime() default 5000L; // 락 획득을 시도하는 최대 시간 (ms)
    long leaseTime() default 3000L; // 락을 획득한 후, 점유하는 최대 시간 (ms)
}
