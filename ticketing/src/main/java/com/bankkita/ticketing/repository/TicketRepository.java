package com.bankkita.ticketing.repository;

// Kontrak penyimpanan tiket yang digunakan oleh service.
import com.bankkita.ticketing.domain.Ticket;

public interface TicketRepository {
    Ticket save(Ticket ticket);
}
