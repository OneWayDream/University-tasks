package ru.itis.cloudlab.cloudphoto.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.cloudlab.cloudphoto.cloud.YandexCloudBucketWorker;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ListCommand implements ConsoleCommand {

    private final YandexCloudBucketWorker yandexCloudBucketWorker;
    private String albumName;

    @Override
    public void execute() {
        initYandexCloudBucketWorker();
        boolean isAlbumDefined = albumName != null;
        if (isAlbumDefined){
            showAlbumContent();
        } else {
            showAlbums();
        }
    }

    private void initYandexCloudBucketWorker() {
        yandexCloudBucketWorker.init();
    }

    private void showAlbums(){
        List<String> albums = yandexCloudBucketWorker.getAlbums();
        if (albums.isEmpty()){
            throw new IllegalArgumentException("Bucket doesn't contain a single albums.");
        }
        albums.forEach(System.out::println);
    }

    private void showAlbumContent(){
        List<String> albumPhotos = yandexCloudBucketWorker.getAlbumPhotos(albumName);
        checkIsAlbumIsNotExists(albumPhotos);
        checkIsAlbumIsEmpty(albumPhotos);
        albumPhotos.forEach(System.out::println);
    }

    private void checkIsAlbumIsNotExists(List<String> album){
        if (album.isEmpty()){
            throw new IllegalArgumentException("Album doesn't exist.");
        }
    }

    private void checkIsAlbumIsEmpty(List<String> album){
        if (album.size() == 1 && album.get(0).equals("")){
            throw new IllegalArgumentException("Album doesn't contain a single photo.");
        }
    }

    @Override
    public Command getCommandType() {
        return Command.LIST;
    }

    @Override
    public void setArguments(Map<String, String> arguments) {
        albumName = arguments.get("album");
    }

}

