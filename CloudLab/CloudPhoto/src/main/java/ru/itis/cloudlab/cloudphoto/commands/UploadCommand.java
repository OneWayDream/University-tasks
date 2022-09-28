package ru.itis.cloudlab.cloudphoto.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.cloudlab.cloudphoto.cloud.YandexCloudBucketWorker;
import ru.itis.cloudlab.cloudphoto.entities.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UploadCommand implements ConsoleCommand {

    private final YandexCloudBucketWorker yandexCloudBucketWorker;
    private String albumName;
    private String albumFolderPath;

    @Override
    public void execute() {
        initYandexCloudBucketWorker();
        initCurrentPath();
        File albumDirectory = getAlbumDirectory();
        boolean anyUploads = false;
        List<File> filesToUpload = new ArrayList<>();
        for (File file : Objects.requireNonNull(albumDirectory.listFiles())){
            if (checkIsImage(file)){
                filesToUpload.add(file);
                anyUploads = true;
            }
        }
        yandexCloudBucketWorker.uploadFiles(albumName, filesToUpload);
        if (!anyUploads){
            throw new IllegalArgumentException("No image to upload");
        }
    }

    private void initYandexCloudBucketWorker(){
        yandexCloudBucketWorker.init();
    }

    private void initCurrentPath(){
        if (albumFolderPath == null){
            albumFolderPath = System.getProperty("user.dir");
        }
    }

    private File getAlbumDirectory(){
        File albumDirectory = new File(albumFolderPath);
        if (!validateFileAsAlbumDirectory(albumDirectory)){
            throw new IllegalArgumentException("Can't handle this path as an album directory.");
        }
        return albumDirectory;
    }

    private boolean validateFileAsAlbumDirectory(File file){
        return checkIsFileIsDirectory(file) && checkIsDirectoryHaveContent(file);
    }

    private boolean checkIsFileIsDirectory(File file){
        return file.isDirectory();
    }

    private boolean checkIsDirectoryHaveContent(File directory){
        return directory.listFiles() != null;
    }

    private boolean checkIsImage(File file){
        String[] fileParts = file.getName().split("\\.");
        return fileParts.length > 1 && Image.isImage(fileParts[1]);
    }

    @Override
    public Command getCommandType() {
        return Command.UPLOAD;
    }

    @Override
    public void setArguments(Map<String, String> arguments) {
        albumName = arguments.get("album");
        albumFolderPath = arguments.get("path");
        checkIfAlbumNameIsNull();

    }
    private void checkIfAlbumNameIsNull(){
        if (albumName == null) {
            throw new IllegalArgumentException("Argument '--ALBUM' is required for this command.");
        }
    }

}
