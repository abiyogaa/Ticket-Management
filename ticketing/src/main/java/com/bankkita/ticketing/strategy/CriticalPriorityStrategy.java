package com.bankkita.ticketing.strategy;

import com.bankkita.ticketing.domain.Ticket;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

// Daftarkan strategy agar dapat digunakan oleh TicketService.
@Component
public class CriticalPriorityStrategy implements PriorityStrategy {

    private static final List<String> CRITICAL_KEYWORDS = Arrays.asList(
            "fraud", "unauthorized", "hack", "hacked", "scam", "stolen");
    private static final List<Ticket.TicketCategory> HIGH_PRIORITY_CATEGORIES = Arrays.asList(
            Ticket.TicketCategory.ACCOUNT_ISSUE,
            Ticket.TicketCategory.TRANSACTION_ISSUE);

    @Override
    public Ticket.Priority calculatePriority(Ticket ticket) {
        String description = ticket.getDescription().toLowerCase();

        // Check for CRITICAL keywords
        for (String keyword : CRITICAL_KEYWORDS) {
            if (description.contains(keyword)) {
                return Ticket.Priority.CRITICAL;
            }
        }

        // Check for HIGH priority categories
        if (HIGH_PRIORITY_CATEGORIES.contains(ticket.getCategory())) {
            return Ticket.Priority.HIGH;
        }

        // Check for MEDIUM priority (SERVICE_QUALITY)
        if (ticket.getCategory() == Ticket.TicketCategory.SERVICE_QUALITY) {
            return Ticket.Priority.MEDIUM;
        }

        // Default to LOW (FEEDBACK)
        return Ticket.Priority.LOW;
    }
}
