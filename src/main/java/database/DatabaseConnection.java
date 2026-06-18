package database;

import utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class DatabaseConnection {

    private Connection connection;

    public void connect(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            log.error("Failed to connect to {}", url);
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            log.error("Failed to close connection");
            throw new RuntimeException(e);
        }
    }

    private String calculateQueryDuration(long queryStartTime) {
        double queryDuration = (System.nanoTime() - queryStartTime) / 1_000_000_000.0;
        return String.format("%.6f", queryDuration);
    }

    public void executeQuery(String query) {
        int attempt = 0;
        int maxRetries = 10;
        int timeoutSeconds = 15;

        while (true) {
            attempt++;
            long queryStartTime = System.nanoTime();
            PreparedStatement ps = null;

            try {
                log.info("Query attempt {}/{}, timeout set to {}s: {}", attempt, maxRetries, timeoutSeconds, query);

                ps = connection.prepareStatement(query);
                ps.setQueryTimeout(timeoutSeconds);
                int rowsAffected = ps.executeUpdate();
                String formattedQueryDuration = calculateQueryDuration(queryStartTime);

                log.info("Successfully executed query on attempt {} -> rows affected: {}, time: {}s", attempt, rowsAffected, formattedQueryDuration);
                return;

            } catch (SQLException e) {
                log.warn("Query attempt {}/{} failed with SQLState {}: {}", attempt, maxRetries, e.getSQLState(), e.getMessage());

                if (attempt >= maxRetries) {
                    log.error("All {} query attempts exhausted for: {}", maxRetries, query);
                    throw new RuntimeException("Query failed after " + maxRetries + " attempts", e);
                }
                Utils.sleep(1000);
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        log.warn("Failed to close PreparedStatement: {}", e.getMessage());
                    }
                }
            }
        }
    }

    public ResultSet executeQueryWithResultSet(String query) {
        long queryStartTime = System.nanoTime();
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(query);
            String formattedQueryDuration = calculateQueryDuration(queryStartTime);

            log.info("Successfully executed query: {}, execution time: {}s", query, formattedQueryDuration);
            return resultSet;

        } catch (SQLException e) {
            log.error("Failed to execute query: {}", query);
            throw new RuntimeException("Database query execution failed.", e);
        }
    }

    public int countResultSetRecords(ResultSet resultSet) {
        try {
            resultSet.last();
            int resultsRows = resultSet.getRow();
            resultSet.beforeFirst();
            return resultsRows;
        } catch (SQLException e) {
            log.error("Error counting records: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}