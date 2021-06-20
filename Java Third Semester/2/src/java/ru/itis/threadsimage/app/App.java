package ru.itis.threadsimage.app;

import ru.itis.threadsimage.app.ThreadsImages;

import com.beust.jcommander.JCommander;



public class App {
    public static void main(String[] args) {
        ThreadsImages threadsimages = new ThreadsImages();
        JCommander.newBuilder()
                .addObject(threadsimages)
                .build()
                .parse(args);
        threadsimages.run();
    }
}
