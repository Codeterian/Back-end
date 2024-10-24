package com.codeterian.queue.application.service;

import com.codeterian.common.infrastructure.util.Passport;
import com.codeterian.queue.infrastructure.redisson.aspect.DistributedLock;
import com.codeterian.queue.presentation.dto.QueueResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueService {

    private final RedisTemplate<String, String> redisTemplate;

    private final SimpMessagingTemplate simpMessagingTemplate;

    // 대기열 임계치 설정
    private static final int TRAFFIC_THRESHOLD = 100; // 대기 없이 최대 100명까지 처리

    private static final String WAITING_QUEUE = "WaitingQueue";

    private static final String RUNNING_QUEUE = "RunningQueue";


    /**
     * 사용자를 대기큐 or 실행큐에 추가 (새로고침 시 뒤로 밀리도록 처리)
     */
    @DistributedLock(key = "#lockName")
    public void joinQueue(Passport passport) {
        String userId = passport.getUserId().toString();

        // 실행큐와 대기큐에서 기존 사용자 제거
        redisTemplate.opsForZSet().remove(RUNNING_QUEUE, userId);
        redisTemplate.opsForZSet().remove(WAITING_QUEUE, userId);

        Long currentRunningQueueSize = redisTemplate.opsForZSet().size(RUNNING_QUEUE);

        if (currentRunningQueueSize != null && currentRunningQueueSize > TRAFFIC_THRESHOLD-1) {
            redisTemplate.opsForZSet().add(WAITING_QUEUE, userId, System.currentTimeMillis());
            log.info("사용자가 대기큐에 추가되었습니다.");

        } else {
            // 실행큐에 사용자 추가
            redisTemplate.opsForZSet().add(RUNNING_QUEUE, userId, System.currentTimeMillis());
            log.info("사용자가 실행큐에 추가되었습니다.");
        }

        // 대기열에서의 순서 확인
        Long waitingPosition = getQueuePosition(userId);
        log.info("Waiting Position: " + waitingPosition.toString());

        // WebSocket을 통해 사용자에게 순위 전송
        sendQueuePosition(userId, waitingPosition);
    }

    /**
     * 대기큐에서 사용자를 꺼내서 실행큐로 이동
     * 스케줄러로 실행하여 대기큐에서의 대기 시간을 최소화
     */
    @Scheduled(fixedDelay = 2000)
    public void getNextUserFrom() {
        Set<String> nextUsers = redisTemplate.opsForZSet().range(WAITING_QUEUE, 0, 0);
        Long currentRunningQueueSize = redisTemplate.opsForZSet().size(RUNNING_QUEUE);

        if (nextUsers != null && !nextUsers.isEmpty() &&
                currentRunningQueueSize != null && currentRunningQueueSize < TRAFFIC_THRESHOLD) {
            String nextUser = nextUsers.iterator().next();
            redisTemplate.opsForZSet().remove(WAITING_QUEUE, nextUser);

            //실행큐로 이동
            redisTemplate.opsForZSet().add(RUNNING_QUEUE, nextUser, System.currentTimeMillis());

            // 대기열에서의 순서 확인
            Long waitingPosition = redisTemplate.opsForZSet().rank(RUNNING_QUEUE, nextUser);
            sendQueuePosition(nextUser, waitingPosition);

        }
    }

    public Long getQueuePosition(String userId) {
        Long position = redisTemplate.opsForZSet().rank(WAITING_QUEUE, userId);

        if (position != null) {
            return position + 101; // 대기큐에서의 순위 반환 (0 기반이므로 1을 더함)
        }

        // 대기큐에 없을 경우 실행큐에서 순위 확인
        position = redisTemplate.opsForZSet().rank(RUNNING_QUEUE, userId);

        if (position != null) {
            return position + 1; // 실행큐에서의 순위 반환
        }

        // 사용자 대기열과 실행큐 모두에서 순위를 찾지 못했을 때 예외 처리
        throw new IllegalStateException("사용자를 찾을 수 없습니다: " + userId);
    }


    /**
     * WebSocket으로 대기열 순위 전송
     */
    private void sendQueuePosition(String userId, Long position) {
        simpMessagingTemplate.convertAndSendToUser(userId,
                "/queue/position", new QueueResponseDto(position));
    }

    /**
     * 실패 알림을 사용자에게 전송하는 메서드
     */
    private void sendFailureNotification(String userId) {
        simpMessagingTemplate.convertAndSendToUser(
                userId, "/queue/errors", "대기열 접속은 성공했으나 주문 처리에 실패했습니다. 다시 시도해 주세요.");
    }


}
