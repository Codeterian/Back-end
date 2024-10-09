package com.codeterian.ticket.domain.repository;

import com.codeterian.ticket.domain.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Optional<Ticket> findByIdAndDeletedAtIsNull(UUID id);

    List<Ticket> findAllByDeletedAtIsNull();

    Optional<Ticket> findBySeatSectionAndSeatNumberAndDeletedAtIsNull (String seatSection, int seatNumber);

}
