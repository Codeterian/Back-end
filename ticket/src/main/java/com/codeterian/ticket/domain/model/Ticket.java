package com.codeterian.ticket.domain.model;

import com.codeterian.common.infrastructure.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ticket")
public class Ticket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "performance_id", nullable = false)
    private UUID performanceId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "seat_section")
    private String seatSection;

    @Column(name = "seat_number")
    private Integer seatNumber;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private UUID orderID;


    public static Ticket create(UUID performanceId, TicketStatus ticketStatus, Integer price, String seatSection, Integer seatNumber, Long userId, UUID orderID) {
       return new Ticket(performanceId, ticketStatus, price, seatSection, seatNumber, userId, orderID);
    }

    private Ticket(UUID performanceId, TicketStatus ticketStatus, Integer price, String seatSection, Integer seatNumber, Long userId, UUID orderId) {
        this.performanceId = performanceId;
        this.ticketStatus = ticketStatus;
        this.price = price;
        this.seatSection = seatSection;
        this.seatNumber = seatNumber;
        this.userId = userId;
        this.orderID = orderId;
    }

    public void update(String seatSection, int seatNumber, TicketStatus ticketStatus, Integer price) {
        this.seatSection = seatSection;
        this.seatNumber = seatNumber;
        this.ticketStatus = ticketStatus;
        this.price = price;
    }

}
