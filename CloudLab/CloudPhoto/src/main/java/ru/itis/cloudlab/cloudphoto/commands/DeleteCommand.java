package ru.itis.cloudlab.cloudphoto.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.cloudlab.cloudphoto.cloud.YandexCloudBucketWorker;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DeleteCommand implements ConsoleCommand {
    private final YandexCloudBucketWorker yandexCloudBucketWorker;
    private String albumName;
    private String photoToDelete;

    @Override
    public void execute() {
        initYandexCloudBucketWorker();
        if (photoToDelete == null){
            yandexCloudBucketWorker.deleteAlbum(albumName);
        } else {
            yandexCloudBucketWorker.deletePhoto(albumName, photoToDelete);
        }
    }
    private void initYandexCloudBucketWorker(){
        yandexCloudBucketWorker.init();
    }

    @Override
    public Command getCommandType() {
        return Command.DELETE;
    }

    @Override
    public void setArguments(Map<String, String> arguments) {
        albumName = arguments.get("album");
        photoToDelete = arguments.get("photo");
        checkIfAlbumNameIsNull();
    }

    private void checkIfAlbumNameIsNull(){
        if (albumName == null) {
            throw new IllegalArgumentException("Argument '--ALBUM' is required for this command.");
        }
    }

}