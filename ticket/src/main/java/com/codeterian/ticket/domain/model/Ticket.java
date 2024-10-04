package com.codeterian.ticket.domain.model;

import com.codeterian.common.infrastructure.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ticket")
public class Ticket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Getter
    private UUID id;

    @Column(name = "performance_id", nullable = false)
    @Getter
    private UUID performanceId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Getter
    private TicketStatus ticketStatus;

    @Column(nullable = false)
    @Getter
    private Integer price;

    @Column(name = "seat_section")
    @Getter
    private String seatSection;

    @Column(name = "seat_number")
    @Getter
    private String seatNumber;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    public static Ticket create(UUID performanceId, TicketStatus ticketStatus, Integer price, String seatSection, String seatNumber, UUID userId) {
       return new Ticket(performanceId, ticketStatus, price, seatSection, seatNumber,userId);
    }

    private Ticket(UUID performanceId, TicketStatus ticketStatus, Integer price, String seatSection, String seatNumber, UUID userId) {
        this.performanceId = performanceId;
        this.ticketStatus = ticketStatus;
        this.price = price;
        this.seatSection = seatSection;
        this.seatNumber = seatNumber;
        this.userId = userId;
    }

    public void update(String seatSection, String seatNumber, TicketStatus ticketStatus, Integer price) {
        this.seatSection = seatSection;
        this.seatNumber = seatNumber;
        this.ticketStatus = ticketStatus;
        this.price = price;
    }

}
