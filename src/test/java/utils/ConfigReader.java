package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            properties = new Properties();
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException("config.properties not found. " + e.getMessage());
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) throw new RuntimeException("Key '" + key + "' not found in config.properties");
        return value;
    }

    public static boolean getBoolean(String key) { return Boolean.parseBoolean(get(key)); }
    public static int getInt(String key) { return Integer.parseInt(get(key)); }
}