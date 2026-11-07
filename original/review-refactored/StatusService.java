package com.bankkita.ticketing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class StatusService {

    private final JdbcTemplate jdbcTemplate;
    private static final Map<String, Integer> PRIORITY_SCORES = Map.of(
        "CRITICAL", 100,
        "HIGH", 75,
        "MEDIUM", 50,
        "LOW", 25
    );

    @Autowired
    public StatusService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // FIX: SQL Injection vulnerability dengan PreparedStatement
    public List<String> getTicketsByStatus(String status) {
        String query = "SELECT description FROM tickets WHERE status = ?";
        return jdbcTemplate.queryForList(query, String.class, status);
    }

    // FIX: N+1 query problem dengan batch update
    @Transactional
    public void updateAllTicketStatuses(List<Long> ticketIds, String newStatus) {
        String query = "UPDATE tickets SET status = ? WHERE id = ANY(?)";

        Long[] idsArray = ticketIds.toArray(new Long[0]);
        jdbcTemplate.update(query, newStatus, idsArray);
    }

    // FIX: Code complexity dengan Map
    public int calculatePriorityScore(String priority) {
        return PRIORITY_SCORES.getOrDefault(priority, 0);
    }
}
