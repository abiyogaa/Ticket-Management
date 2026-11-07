package com.bankkita.ticketing.service;

import com.bankkita.ticketing.domain.Ticket;
import com.bankkita.ticketing.repository.TicketRepository;
import com.bankkita.ticketing.strategy.CriticalPriorityStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    private TicketService ticketService;
    private CriticalPriorityStrategy priorityStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        priorityStrategy = new CriticalPriorityStrategy();
        ticketService = new TicketService(ticketRepository, priorityStrategy);
    }

    @Test
    @DisplayName("Should assign CRITICAL priority when description contains 'fraud'")
    void testCreateTicket_CriticalPriority_Fraud() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setCustomerName("John Doe");
        ticket.setEmail("john.doe@email.com");
        ticket.setCategory(Ticket.TicketCategory.ACCOUNT_ISSUE);
        ticket.setDescription("I suspect fraud activity on my account");

        // Kembalikan tiket dari service agar test memeriksa priority yang benar-benar
        // dihitung.
        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ticket result = ticketService.createTicket(ticket);

        // Assert
        assertNotNull(result);
        assertEquals(Ticket.Priority.CRITICAL, result.getPriority());
        assertEquals(Ticket.TicketStatus.OPEN, result.getStatus());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    @DisplayName("Should assign HIGH priority for TRANSACTION_ISSUE category")
    void testCreateTicket_HighPriority_Transaction() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setCustomerName("Jane Smith");
        ticket.setEmail("jane.smith@email.com");
        ticket.setCategory(Ticket.TicketCategory.TRANSACTION_ISSUE);
        ticket.setDescription("My transfer failed but money was deducted");

        // Kembalikan tiket dari service agar test memeriksa priority yang benar-benar
        // dihitung.
        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ticket result = ticketService.createTicket(ticket);

        // Assert
        assertEquals(Ticket.Priority.HIGH, result.getPriority());
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    @DisplayName("Should assign MEDIUM priority for SERVICE_QUALITY category")
    void testCreateTicket_MediumPriority_ServiceQuality() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setCustomerName("Bob Wilson");
        ticket.setEmail("bob.w@email.com");
        ticket.setCategory(Ticket.TicketCategory.SERVICE_QUALITY);
        ticket.setDescription("Customer service was slow today");

        // Kembalikan tiket dari service agar test memeriksa priority yang benar-benar
        // dihitung.
        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ticket result = ticketService.createTicket(ticket);

        // Assert
        assertEquals(Ticket.Priority.MEDIUM, result.getPriority());
    }

    @Test
    @DisplayName("Should assign LOW priority for FEEDBACK category")
    void testCreateTicket_LowPriority_Feedback() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setCustomerName("Alice Brown");
        ticket.setEmail("alice.b@email.com");
        ticket.setCategory(Ticket.TicketCategory.FEEDBACK);
        ticket.setDescription("Great mobile app experience!");

        // Kembalikan tiket dari service agar test memeriksa priority yang benar-benar
        // dihitung.
        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ticket result = ticketService.createTicket(ticket);

        // Assert
        assertEquals(Ticket.Priority.LOW, result.getPriority());
    }

    @Test
    @DisplayName("Should throw exception when customer name is null")
    void testCreateTicket_InvalidInput_NullCustomerName() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setEmail("test@email.com");
        ticket.setCategory(Ticket.TicketCategory.FEEDBACK);
        ticket.setDescription("Test description");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            ticketService.createTicket(ticket);
        });
        verify(ticketRepository, never()).save(any(Ticket.class));
    }
}
