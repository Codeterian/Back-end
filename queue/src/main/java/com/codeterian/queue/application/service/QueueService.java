package com.codeterian.queue.application.service;

import com.codeterian.queue.application.feign.OrderService;
import com.codeterian.queue.application.feign.dto.OrderAddRequestDto;
import com.codeterian.queue.application.feign.dto.OrderAddResponseDto;
import com.codeterian.queue.presentation.dto.QueueResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final RedisTemplate<String, String> redisTemplate;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final ObjectMapper objectMapper;

    private final OrderService orderService;

    // 대기열 임계치 설정
    private static final int TRAFFIC_THRESHOLD = 100; // 대기 없이 최대 100명까지 처리

    private static final String WAITING_QUEUE = "WaitingQueue";

    private static final String RUNNING_QUEUE = "RunningQueue";

    private static final String ORDER_REQUEST = "OrderRequest:";

    /**
     * 사용자를 대기큐 or 실행큐에 추가, requestDto를 userId를 키로 해서 저장
     */
    public void joinQueue(Long userId, OrderAddRequestDto requestDto) {
        Long currentRunningQueueSize = redisTemplate.opsForZSet().size(RUNNING_QUEUE);

        redisTemplate.opsForValue().set(ORDER_REQUEST + userId.toString(), convertToJson(requestDto));

        if (currentRunningQueueSize != null && currentRunningQueueSize >= TRAFFIC_THRESHOLD) {
            //임계치 초과: 대기큐에 사용자 추가
            redisTemplate.opsForZSet().add(WAITING_QUEUE, userId.toString(), System.currentTimeMillis());


            // 대기열에서의 순서 확인
            Long waitingPosition = redisTemplate.opsForZSet().rank(WAITING_QUEUE, userId);
            sendQueuePosition(userId, waitingPosition);
        } else {
            //실행큐에 추가
            redisTemplate.opsForZSet().add(RUNNING_QUEUE, userId.toString(), System.currentTimeMillis());

            processNextUserInRunningQueue();
        }
    }

    /**
     * 대기큐에서 사용자를 꺼내서 실행큐로 이동
     * 스케줄러로 실행하여 대기큐에서의 대기 시간을 최소화
     */
    @Scheduled(fixedRate = 5000)
    public void getNextUserFrom() {
        Set<String> nextUsers = redisTemplate.opsForZSet().range(WAITING_QUEUE, 0, 0);

        if (nextUsers != null && !nextUsers.isEmpty()) {
            String nextUser = nextUsers.iterator().next();
            redisTemplate.opsForZSet().remove(WAITING_QUEUE, nextUser);

            //실행큐로 이동
            redisTemplate.opsForZSet().add(RUNNING_QUEUE, nextUser, System.currentTimeMillis());
        }
    }

    /**
     * WebSocket으로 대기열 순위 전송
     */
    private void sendQueuePosition(Long userId, Long position) {
        simpMessagingTemplate.convertAndSendToUser(userId.toString(),
                "/queue/position", new QueueResponseDto(position));
    }

    /**
     * 실패 알림을 사용자에게 전송하는 메서드
     */
    private void sendFailureNotification(String userId) {
        simpMessagingTemplate.convertAndSendToUser(
                userId, "/queue/errors", "대기열 접속은 성공했으나 주문 처리에 실패했습니다. 다시 시도해 주세요.");
    }

    public void processNextUserInRunningQueue() {
        Set<String> nextUsers = redisTemplate.opsForZSet().range(RUNNING_QUEUE, 0, 0);
        if (nextUsers != null && !nextUsers.isEmpty()) {
            String nextUser = nextUsers.iterator().next();

            //redis에서 해당 유저의 reqeustDto를 가져와야 됨.
            String requestJson = redisTemplate.opsForValue().get(ORDER_REQUEST + nextUser);

            // 주문 처리
            processOrder(nextUser, convertFromJson(requestJson));
        }
    }

    /**
     * 주문 처리 로직
     */
    private void processOrder(String userId, OrderAddRequestDto requestDto) {
        OrderAddResponseDto responseDto = orderService.orderAdd(requestDto).getData();

        if (responseDto != null && responseDto.orderId() != null) {
            //처리된 사용자의 정보 삭제
            redisTemplate.opsForZSet().remove(RUNNING_QUEUE, userId);
            redisTemplate.delete(ORDER_REQUEST + userId);
        } else {
            sendFailureNotification(userId);
        }
    }

    /**
     * RequestDto를 Json 문자열로 변환, Json 문자열을 reqeustDto로 변환
     */
    private String convertToJson(OrderAddRequestDto requestDto) {
        try {
            return objectMapper.writeValueAsString(requestDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert requestDto to JSON", e);
        }
    }

    private OrderAddRequestDto convertFromJson(String json) {
        try {
            return objectMapper.readValue(json, OrderAddRequestDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to requestDto", e);
        }
    }

}
