package main.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configuration data holder for a single environment.
 * Used internally by ConfigManager for YAML deserialization.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvironmentConfig {

    private String baseUrl;
    private String apiBaseUrl;
    private String dbConnection;
    private String adminUsername;
    private String adminPassword;
}
