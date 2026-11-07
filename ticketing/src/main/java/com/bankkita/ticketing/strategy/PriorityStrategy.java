package com.bankkita.ticketing.strategy;

import com.bankkita.ticketing.domain.Ticket;

public interface PriorityStrategy {
    Ticket.Priority calculatePriority(Ticket ticket);
}
