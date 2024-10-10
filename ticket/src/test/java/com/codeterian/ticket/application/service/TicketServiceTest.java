package com.codeterian.ticket.application.service;

import com.codeterian.ticket.application.feign.PerformanceService;
import com.codeterian.ticket.domain.model.Ticket;
import com.codeterian.ticket.domain.model.TicketStatus;
import com.codeterian.ticket.domain.repository.TicketRepository;
import com.codeterian.ticket.presentation.dto.request.TicketAddRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest // 스프링 컨텍스트 로드
public class TicketServiceTest {

    @Autowired
    private TicketService ticketService; // 실제 빈을 사용

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private PerformanceService performanceService;

    @MockBean
    private RedissonClient redissonClient;

    @MockBean
    private RLock rLock;

    @Test
    void _분산락_실행_성공_테스트() throws Throwable {
        // Given
        UUID performanceId = UUID.randomUUID();
        TicketAddRequestDto requestDto = new TicketAddRequestDto(performanceId, TicketStatus.BOOKING, 100000,
                "A", 12);

        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);

        when(ticketRepository.findBySeatSectionAndSeatNumberAndDeletedAtIsNull(anyString(), anyInt()))
                .thenReturn(Optional.empty());

        // When
        ticketService.addTicket(requestDto);

        // Then
        verify(ticketRepository).save(any(Ticket.class));
        verify(performanceService).decreaseTicketStock(performanceId);
        verify(rLock).unlock(); // 락 해제 검증
    }
}
