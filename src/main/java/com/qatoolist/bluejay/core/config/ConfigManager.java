package com.qatoolist.bluejay.core.config;

import com.qatoolist.bluejay.core.exceptions.ConfigLoadException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * ConfigManager provides loading and access to configuration properties.
 * Configurations are loaded from multiple sources :
 * 1. A default configuration file ("src/test/resources/config/default.properties").
 * 2. An environment-specific configuration file (if existing).
 * 3. Environment variables (highest priority, overriding properties).
 */
public class ConfigManager {
    private static final Properties properties = new Properties();
    private static final String DEFAULT_CONFIG_PATH = "src/test/resources/config/default.properties";

    private static final String ENV = System.getProperty("AUTO_ENV", "default");
    private static final Integer DEFAULT_TIMEOUT = Integer.valueOf(System.getProperty("timeout", "10"));
    private static String baseURL = "";

    private static final Logger logger = LogManager.getLogger(ConfigManager.class);

    static {
        loadDefaultConfigurations();
        loadEnvironmentSpecificConfigurations();
        baseURL = getProperty("app.base_url");
    }

    private ConfigManager() {
    } // Prevent instantiation

    /**
     * Returns the current environment being used for configuration (retrieved from 'AUTO_ENV').
     *
     * @return The environment name.
     */
    public static String getEnv() {
        return ENV.toLowerCase();
    }

    /**
     * Returns the default timeout value (in seconds), retrieved from a system property
     * ('timeout') or falling back to 10 seconds if not configured.
     *
     * @return The default timeout in seconds.
     */
    public static Integer getDefaultTimeout() {
        return DEFAULT_TIMEOUT;
    }

    /**
     * Returns the configured base URL for the application (from 'app.base_url' property).
     *
     * @return The base URL.
     */
    public static String getBaseUrl() {
        return baseURL;
    }

    /**
     * Loads default configuration properties from the specified path.
     *
     * @throws ConfigLoadException if an error occurs loading the default configuration.
     */
    private static void loadDefaultConfigurations() {
        try (FileInputStream defaultConfigStream = new FileInputStream(DEFAULT_CONFIG_PATH)) {
            properties.load(defaultConfigStream);
        } catch (IOException e) {
            throw new ConfigLoadException("Failed to load default configuration.", e);
        }
    }

    /**
     * Loads environment-specific configuration properties (if the file exists).
     * Environment is retrieved from the "AUTO_ENV" system property (defaults to "default").
     *
     * @throws ConfigLoadException if an error occurs loading environment-specific configuration.
     */
    private static void loadEnvironmentSpecificConfigurations() {
        String configFilePath = "src/test/resources/config/" + ENV + ".properties";

        if (Files.exists(Paths.get(configFilePath))) {
            try (FileInputStream envConfigStream = new FileInputStream(configFilePath)) {
                properties.load(envConfigStream);
            } catch (IOException e) {
                throw new ConfigLoadException("Failed to load configuration for environment: " + ENV, e);
            }
        }
    }

    /**
     * Reloads all configurations - clears existing properties and applies the loading logic.
     */
    public static void reloadConfigurations() {
        properties.clear();
        loadDefaultConfigurations();
        loadEnvironmentSpecificConfigurations();
    }

    /**
     * Gets a configuration property, checking environment variables first, then properties.
     *
     * @param key Property key
     * @return Property value if found, otherwise null
     */
    public static String getProperty(String key) {
        String envOverride = System.getenv(key.toUpperCase().replace(".", "_"));
        return envOverride != null ? envOverride : properties.getProperty(key);
    }

    /**
     * Checks whether a configuration property exists in either the loaded properties
     * or as a system environment variable.
     *
     * @param key The name of the property to check.
     * @return true if the property exists, false otherwise.
     */
    public static boolean hasProperty(String key) {
        String envOverride = System.getenv(key.toUpperCase().replace(".", "_"));
        return envOverride != null || properties.containsKey(key);
    }

    /**
     * Gets a configuration property with a default value.
     *
     * @param key          Property key
     * @param defaultValue Value to return if the property is not found
     * @return Property value if found, otherwise the provided defaultValue
     */
    public static String getOptionalProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Gets a configuration property as an Integer.
     *
     * @param key Property key
     * @return Integer value if found and parsable, otherwise returns null
     */
    public static Integer getIntProperty(String key) {
        String value = getProperty(key);
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            String error = "Invalid format for key: " + key + ", using default value: null";
            logger.error(error);
            return null;
        }
    }

    /**
     * Gets a configuration property as an Integer.
     *
     * @param key          Property key
     * @param defaultValue Value to return if the property is not found or cannot be parsed
     * @return Integer value if found and parsable, otherwise the provided defaultValue
     */
    public static Integer getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            String error = "Invalid format for key: " + key + ", using default value: " + defaultValue;
            logger.error(error);
            return defaultValue;
        }
    }

    /**
     * Gets a configuration property as a Boolean.
     *
     * @param key Property key
     * @return Boolean value if found and parsable, otherwise returns null
     */
    public static Boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        return value != null ? Boolean.valueOf(value) : null;
    }


    /**
     * Gets a configuration property as a Boolean.
     *
     * @param key          Property key
     * @param defaultValue Value to return if the property is not found or cannot be parsed
     * @return Boolean value if found and parsable, otherwise the provided defaultValue
     */
    public static Boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.valueOf(value) : defaultValue;
    }

    /**
     * Gets all configuration properties for a specified browser.
     *
     * @param browserName The name of the browser (e.g., "chrome", "firefox").
     * @return Properties object containing all browser-specific configuration properties.
     */
    public static Properties getBrowserProperties(String browserName) {
        Properties browserProperties = new Properties();
        final String prefix = "browser." + browserName.toLowerCase() + ".";

        properties.stringPropertyNames().stream()
                .filter(key -> key.startsWith(prefix))
                .forEach(key -> browserProperties.setProperty(key.substring(prefix.length()), getProperty(key)));

        return browserProperties;
    }


}
