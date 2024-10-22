package com.codeterian.queue.infrastructure.redisson.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AopForTransaction {

    /**
    부모 트랜잭션에 관계없이 별도의 트랜잭션으로 동작하도록 설정하여 반드시 트랜잭션 커밋 이후
    락이 해제되도록 REQUIRES_NEW 옵션 부여
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

}
