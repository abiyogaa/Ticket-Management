package com.bankkita.ticketing.service;

import com.bankkita.ticketing.domain.Ticket;
import com.bankkita.ticketing.repository.TicketRepository;
import com.bankkita.ticketing.strategy.PriorityStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final PriorityStrategy priorityStrategy;

    @Autowired
    public TicketService(TicketRepository ticketRepository,
            PriorityStrategy priorityStrategy) {
        this.ticketRepository = ticketRepository;
        this.priorityStrategy = priorityStrategy;
    }

    public Ticket createTicket(Ticket ticket) {
        // Validate input
        validateTicket(ticket);

        // Auto-assign priority using strategy pattern
        Ticket.Priority calculatedPriority = priorityStrategy.calculatePriority(ticket);
        ticket.setPriority(calculatedPriority);

        // Set timestamps
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        // Set initial status
        ticket.setStatus(Ticket.TicketStatus.OPEN);

        // Save to database
        return ticketRepository.save(ticket);
    }

    private void validateTicket(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }
        if (ticket.getCustomerName() == null || ticket.getCustomerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (ticket.getEmail() == null || !ticket.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Valid email is required");
        }
        if (ticket.getCategory() == null) {
            throw new IllegalArgumentException("Category is required");
        }
        if (ticket.getDescription() == null || ticket.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
    }
}
