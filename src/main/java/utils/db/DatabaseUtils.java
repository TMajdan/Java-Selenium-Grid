package utils.db;

import database.DatabaseConnection;
import utils.Utils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseUtils {

    /**
     * @param stringList list of Strings containing values for SQL "IN" clause
     * @return Formatted, ready to use list of Strings wrapped with single quotes and separated by commas.
     */
    public static String formatStringListForSqlInClause(List<String> stringList) {
        return stringList.stream().map(name -> "'" + name + "'").collect(Collectors.joining(", "));
    }

    /**
     * @param integerList list of Integers containing values for SQL "IN" clause
     * @return Formatted, ready to use list of Integers separated by commas.
     */
    public static String formatIntegerListForSqlInClause(List<Integer> integerList) {
        return integerList.stream().map(String::valueOf).collect(Collectors.joining(", "));
    }

    /**
     * Executes database query with retry logic until data is found or max retries exceeded.
     *
     * @param databaseConnection active database connection
     * @param query SQL query to execute
     * @param formattedMsg message context with parameters for logging retry attempts and errors (e.g. "uuid: %s")
     * @param retriesNumber max retry attempts
     * @return ResultSet with cursor set on the 1st data row, or throws exception
     */
    public static ResultSet executeQueryWithRetry(DatabaseConnection databaseConnection, String query, String formattedMsg, int retriesNumber) {
        int retryCount = 0;

        while (retryCount < retriesNumber) {
            try {
                ResultSet rs = databaseConnection.executeQueryWithResultSet(query);
                if (rs.next()) {
                    return rs;
                } else {
                    log.warn("Not found {}. Retrying... Attempt {}/{}", formattedMsg, retryCount + 1, retriesNumber);
                    retryCount++;
                    Utils.sleep(1000);
                }
            } catch (SQLException e) {
                throw new RuntimeException(String.format("SQL error occurred while fetching %s. Error: %s", formattedMsg, e.getMessage()), e);
            }
        }
        throw new IllegalArgumentException(String.format("Failed to retrieve %s after %d attempts.", formattedMsg, retriesNumber));
    }
}