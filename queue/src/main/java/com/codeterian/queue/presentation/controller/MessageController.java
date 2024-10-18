package com.codeterian.queue.presentation.controller;

import com.codeterian.queue.application.service.QueueService;
import com.codeterian.queue.presentation.dto.QueueResponseDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final QueueService queueService;

    public MessageController(QueueService queueService) {
        this.queueService = queueService;
    }

    @MessageMapping("/waiting-system")
    @SendTo("/queue/position")
    public QueueResponseDto handleWaitingSystem(String userId) {
        // 여기서 userId를 기반으로 대기열 순위를 계산하거나 가져오는 로직을 작성
        // 예를 들어, 사용자 순위를 데이터베이스 또는 Redis에서 가져옴
        Long position = queueService.getQueuePosition(userId);

        // 대기열 순위 정보를 클라이언트에게 전송
        return new QueueResponseDto(position);
    }

}
