package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data model representing environment-specific configuration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Environment {

    private String name;
    private String baseUrl;
    private String apiBaseUrl;
    private String dbConnection;
    private String adminUsername;
    private String adminPassword;
}
