package ru.itis.cloudlab.cloudphoto.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class GeneratedPagesUtils {

    private static final String GENERATED_PAGES_FOLDER = "pages";
    public static final String PAGE_EXTENSION = ".html";
    private final String PATH_SEPARATOR = File.separator;

    private static class InstanceHolder {
        private static final GeneratedPagesUtils INSTANCE = new GeneratedPagesUtils();
    }

    public static GeneratedPagesUtils getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private GeneratedPagesUtils(){
        createPagesDirectoryIfNotExists();
    }

    public File createPageFile(String fileName){
        return createAndGetJavaFile(fileName);
    }

    private void createPagesDirectoryIfNotExists(){
        File directory = new File(GENERATED_PAGES_FOLDER);
        if (!directory.exists()){
            directory.mkdir();
        }
    }

    private File createAndGetJavaFile(String fileName){
        File file = new File(GENERATED_PAGES_FOLDER + PATH_SEPARATOR + fileName + PAGE_EXTENSION);
        createFileIfNotExists(file);
        return file;
    }

    private void createFileIfNotExists(File file){
        try{
            file.createNewFile();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Can't create temporary generated pages.", ex);
        }
    }

    public void deletePagesFolder(){
        File directory = new File(GENERATED_PAGES_FOLDER);
        if (directory.exists()){
            try{
                FileUtils.deleteDirectory(directory);
            } catch (Exception ex){
                throw new IllegalArgumentException("Can't delete temporary package", ex);
            }
        }
    }

}
