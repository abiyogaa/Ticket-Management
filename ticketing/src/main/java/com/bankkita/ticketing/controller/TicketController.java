package com.bankkita.ticketing.controller;

import com.bankkita.ticketing.domain.Ticket;
import com.bankkita.ticketing.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        try {
            Ticket createdTicket = ticketService.createTicket(ticket);
            return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Perjelas tipe body kosong agar constructor tidak ambigu pada Spring 7.
            return new ResponseEntity<>((Ticket) null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Gunakan body kosong bertipe Ticket untuk response error.
            return new ResponseEntity<>((Ticket) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
