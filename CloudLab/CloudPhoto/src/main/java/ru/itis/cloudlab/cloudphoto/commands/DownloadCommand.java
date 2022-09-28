package ru.itis.cloudlab.cloudphoto.commands;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import ru.itis.cloudlab.cloudphoto.cloud.YandexCloudBucketWorker;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DownloadCommand implements ConsoleCommand {

    private static final String PATH_SEPARATOR = File.separator;

    private final YandexCloudBucketWorker yandexCloudBucketWorker;
    private String albumName;
    private String downloadFolderPath;

    @Override
    public void execute() {
        initYandexCloudBucketWorker();
        initCurrentPath();
        File destinationDirectory = getDownloadDirectory();
        yandexCloudBucketWorker.downloadAlbum(albumName, destinationDirectory);
        unpackAlbumDirectory(destinationDirectory);
    }

    private void initYandexCloudBucketWorker(){
        yandexCloudBucketWorker.init();
    }
    private void initCurrentPath(){
        if (downloadFolderPath == null){
            downloadFolderPath = System.getProperty("user.dir");
        }
    }

    private File getDownloadDirectory(){
        return new File(downloadFolderPath);
    }

    private void unpackAlbumDirectory(File destinationDirectory){
        try{
            File temporaryDirectory = new File(destinationDirectory.getPath() + PATH_SEPARATOR + albumName);
            if (!temporaryDirectory.exists()){
                throw new IllegalArgumentException("This album doesn't exist.");
            }
            FileUtils.copyDirectory(temporaryDirectory, destinationDirectory);
            FileUtils.deleteDirectory(temporaryDirectory);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Can't work with this directories.", ex);
        }

    }

    @Override
    public Command getCommandType() {
        return Command.DOWNLOAD;
    }

    @Override
    public void setArguments(Map<String, String> arguments) {
        albumName = arguments.get("album");
        downloadFolderPath = arguments.get("path");
        checkIfAlbumNameIsNull();
    }

    private void checkIfAlbumNameIsNull(){
        if (albumName == null) {
            throw new IllegalArgumentException("Argument '--ALBUM' is required for this command.");
        }
    }

}
