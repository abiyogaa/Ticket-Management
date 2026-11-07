package com.bankkita.ticketing.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatusService {

    private String dbUrl = "jdbc:postgresql://localhost:5432/ticketing";
    private String username = "admin";
    private String password = "admin123";

    public List<String> getTicketsByStatus(String status) throws SQLException {
        List<String> tickets = new ArrayList<>();
        Connection conn = DriverManager.getConnection(dbUrl, username, password);

        String query = "SELECT * FROM tickets WHERE status = '" + status + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            tickets.add(rs.getString("description"));
        }

        return tickets;
    }

    public void updateAllTicketStatuses(List<Long> ticketIds, String newStatus) throws SQLException {
        Connection conn = DriverManager.getConnection(dbUrl, username, password);

        for (Long id : ticketIds) {
            String query = "UPDATE tickets SET status = '" + newStatus + "' WHERE id = " + id;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        }
        conn.close();
    }

    public int calculatePriorityScore(String priority) {
        if (priority.equals("CRITICAL")) {
            return 100;
        } else if (priority.equals("HIGH")) {
            return 75;
        } else if (priority.equals("MEDIUM")) {
            return 50;
        } else if (priority.equals("LOW")) {
            return 25;
        } else {
            return 0;
        }
    }
}
