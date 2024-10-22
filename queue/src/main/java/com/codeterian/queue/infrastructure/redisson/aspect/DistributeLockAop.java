package com.codeterian.queue.infrastructure.redisson.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DistributeLockAop {

    private static final String REDISSON_LOCK_PREFIX = "LOCK: ";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.codeterian.queue.infrastructure.redisson.aspect.DistributedLock)")
    public Object redissonLock(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
        RLock rLock = redissonClient.getLock(key);

        try {
            boolean lockable = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
            if (!lockable) {
                log.info("Lock 획득 실패={}", key);
                return false;
            }

            log.info("로직 수행");
            return aopForTransaction.proceed(joinPoint);

        } catch (Throwable e) {
            log.info("에러 발생!");
            throw new RuntimeException(e);
        } finally {
            rLock.unlock();
            log.info("이미 언락 상태 입니다.");
        }
    }

}
