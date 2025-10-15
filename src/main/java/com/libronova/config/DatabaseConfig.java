package com.libronova.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase de configuración para la conexión a la base de datos
 */
public class DatabaseConfig {
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;
    private static String url;
    private static String username;
    private static String password;

    static {
        loadProperties();
    }

    /**
     * Carga las propiedades de configuración desde el archivo config.properties
     */
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("No se pudo encontrar el archivo " + CONFIG_FILE);
            }
            properties.load(input);
            
            url = properties.getProperty("db.url");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");
            
            if (url == null || username == null || password == null) {
                throw new RuntimeException("Faltan propiedades de configuración de base de datos");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la configuración de la base de datos", e);
        }
    }

    /**
     * Obtiene una conexión a la base de datos
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver de MySQL no encontrado", e);
        }
    }

    /**
     * Obtiene una propiedad de configuración
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Obtiene una propiedad de configuración con valor por defecto
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Obtiene un valor entero de configuración
     */
    public static int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Obtiene un valor decimal de configuración
     */
    public static double getDoubleProperty(String key, double defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
