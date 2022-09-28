package ru.itis.cloudlab.cloudphoto.entities;

import java.util.Arrays;

public enum Image {

    JPG("jpg"),
    JPEG("jpeg");

    private final String value;

    Image(String value) {
        this.value = value;
    }

    public static boolean isImage(String value){
        return Arrays.stream(values())
                .anyMatch(image -> image.value.equals(value));
    }
}
