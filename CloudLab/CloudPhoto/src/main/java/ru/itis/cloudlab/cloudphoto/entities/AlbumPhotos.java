package ru.itis.cloudlab.cloudphoto.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AlbumPhotos {

    private List<String> photos;
    private String albumName;
    private String albumUrl;

}
