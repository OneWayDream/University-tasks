package ru.itis.encoding.algorithms.loaders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    protected final static String PROPERTIES_FILE_NAME="app.properties";

    public static Properties loadProperties(){
        try (InputStream input = PropertiesLoader.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME)) {
            Properties properties = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            }

            properties.load(input);
            return properties;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getPropertiesFileName(){
        return PROPERTIES_FILE_NAME;
    }

}
