package ru.itis.cloudlab.cloudphoto.utils;

import org.ini4j.Ini;

import java.io.*;

public class ConfigUtils {

//    private final static String CONFIG_FILE_NAME = "config.ini";
    private final static String CONFIG_FILE_NAME = "cloudphotorc";
    private final static String PATH_SEPARATOR = File.separator;

    private Ini properties;

    private static class InstanceHolder {
        private static final ConfigUtils INSTANCE = new ConfigUtils();
    }

    public static ConfigUtils getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public Ini getConfig(){
        if (!areConfigLoaded()){
            loadConfig();
        }
        return properties;
    }

    public void saveConfig(Ini ini){
        try {
            String pathToProperties = getConfigPath();
            ini.store(new FileOutputStream(pathToProperties));
            properties = ini;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Error during config file writing.", ex);
        }
    }

    private boolean areConfigLoaded(){
        return properties != null;
    }

    private void loadConfig(){
        String configPath = getConfigPath();
        try (InputStream inputStream = new FileInputStream(configPath)) {
            checkLoadedConfigFile(inputStream);
            properties = new Ini(inputStream);
            checkLoadedConfigContent();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Error during config file reading.", ex);
        }
    }
    private String getConfigPath(){
//        return CONFIG_FILE_NAME;
        return System.getProperty("user.home") + PATH_SEPARATOR + ".config" + PATH_SEPARATOR + "cloudphoto"
                + PATH_SEPARATOR + CONFIG_FILE_NAME;
    }

    private void checkLoadedConfigFile(InputStream inputStream){
        if (inputStream == null) {
            throw new IllegalArgumentException("Config file is not found.");
        }
    }

    private void checkLoadedConfigContent(){
        boolean isAllProperties = isPropertyIn("aws_access_key_id") &&
                isPropertyIn("aws_secret_access_key") &&
                isPropertyIn("bucket") &&
                isPropertyIn("region") &&
                isPropertyIn("endpoint_url");
        if (!isAllProperties){
            throw new IllegalArgumentException("There are some properties missing in the config.");
        }
    }

    private boolean isPropertyIn(String key){
        return properties.get("DEFAULT", key) != null;
    }

}
