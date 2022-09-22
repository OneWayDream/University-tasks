package ru.itis.encoding.algorithms.loaders;

import java.util.Properties;

public class SettingsLoader {

    protected static final String INPUT_FILE_PATH_KEY = "input-file-path";
    protected static final String OUTPUT_FILE_PATH_KEY = "output-file-path";
    protected static final String CODE_FILE_PATH_KEY = "code-file-path";
    protected static final String EXTRA_CODE_FILE_PATH_KEY = "extra-code-file-path";
    protected static final String META_FILE_PATH_KEY = "meta-file-path";
    protected static final String EXTRA_META_FILE_PATH_KEY = "extra-meta-file-path";
    protected static final String PROBABILITIES_FILE_PATH_KEY = "probabilities-file-path";
    protected static final String TEXT_LOGGING_KEY = "text-logging";
    protected static final String DICTIONARIES_LOGGING_KEY = "dictionaries-logging";
    protected static final String SIZE_LIMIT_KEY = "size-limit";
    protected static final String ENCODING_ALPHABET_SIZE_KEY = "encoding-alphabet-size";
    protected static final String BLOCK_SIZE_KEY = "block-size";

    protected Properties properties;

    public SettingsLoader(){
        this.properties = PropertiesLoader.loadProperties();
    }

    public String getInputFilePath(){
        return properties.getProperty(INPUT_FILE_PATH_KEY);
    }

    public String getOutputFilePath(){
        return properties.getProperty(OUTPUT_FILE_PATH_KEY);
    }

    public String getCodeFilePath(){
        return properties.getProperty(CODE_FILE_PATH_KEY);
    }

    public String getExtraCodeFilePath(){
        return properties.getProperty(EXTRA_CODE_FILE_PATH_KEY);
    }

    public String getMetaFilePath(){
        return properties.getProperty(META_FILE_PATH_KEY);
    }

    public String getExtraMetaFilePath(){
        return properties.getProperty(EXTRA_META_FILE_PATH_KEY);
    }

    public String getProbabilitiesFilePath(){
        return properties.getProperty(PROBABILITIES_FILE_PATH_KEY);
    }

    public boolean getTextLoggingFlag(){
        return Boolean.parseBoolean(properties.getProperty(TEXT_LOGGING_KEY));
    }

    public boolean getDictionariesLoggingFlag(){
        return Boolean.parseBoolean(properties.getProperty(DICTIONARIES_LOGGING_KEY));
    }

    public int getSizeLimit(){
        return Integer.parseInt(properties.getProperty(SIZE_LIMIT_KEY));
    }

    public int getEncodingAlphabetSize(){
        return Integer.parseInt(properties.getProperty(ENCODING_ALPHABET_SIZE_KEY));
    }

    public int getBlockSize(){
        return Integer.parseInt(properties.getProperty(BLOCK_SIZE_KEY));
    }

}
