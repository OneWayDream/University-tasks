package ru.itis.cloudlab.cloudphoto.generators;

import ru.itis.cloudlab.cloudphoto.entities.AlbumPhotos;

import java.io.File;
import java.util.List;

public interface PagesGenerator {

    File generateAlbumsPage(String fileName, List<String> albums);
    File generateAlbumContentPage(String fileName, AlbumPhotos photos);

    File getErrorPage();

    void close();

}
