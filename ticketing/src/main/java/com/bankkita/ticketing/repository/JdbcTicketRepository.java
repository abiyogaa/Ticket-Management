package com.bankkita.ticketing.repository;

// Menyimpan tiket dan mengambil ID yang dihasilkan database.
import com.bankkita.ticketing.domain.Ticket;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.Statement;
import java.sql.Timestamp;

@Repository
public class JdbcTicketRepository implements TicketRepository {
    private final JdbcTemplate jdbc;

    public JdbcTicketRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Ticket save(Ticket ticket) {
        var key = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            var statement = connection.prepareStatement(
                    "INSERT INTO tickets (customer_name,email,category,description,priority,status,created_at,updated_at) VALUES (?,?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, ticket.getCustomerName());
            statement.setString(2, ticket.getEmail());
            statement.setString(3, ticket.getCategory().name());
            statement.setString(4, ticket.getDescription());
            statement.setString(5, ticket.getPriority().name());
            statement.setString(6, ticket.getStatus().name());
            statement.setTimestamp(7, Timestamp.valueOf(ticket.getCreatedAt()));
            statement.setTimestamp(8, Timestamp.valueOf(ticket.getUpdatedAt()));
            return statement;
        }, key);
        Number id = key.getKey();
        if (id == null)
            throw new IllegalStateException("Database did not generate ticket ID");
        ticket.setId(id.longValue());
        return ticket;
    }
}
