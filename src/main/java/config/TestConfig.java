package config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Central configuration loader that assembles all YAML settings files
 * into a single ConfigManager instance.
 * Delegates to {@link ConfigManager#CONFIG} for the actual configuration.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConfig {

    public static final ConfigManager CONFIG = ConfigManager.CONFIG;
}