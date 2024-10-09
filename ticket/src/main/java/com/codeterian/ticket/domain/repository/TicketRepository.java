package com.codeterian.ticket.domain.repository;

import com.codeterian.ticket.domain.model.Ticket;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Optional<Ticket> findByIdAndDeletedAtIsNull(UUID id);

    List<Ticket> findAllByDeletedAtIsNull();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Ticket> findBySeatSectionAndSeatNumberAndDeletedIsNull (String seatSection, String seatNumber);

}
