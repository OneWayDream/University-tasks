package ru.itis.threadsimage.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ImageDownload implements Runnable {

    protected URI uri;
    protected Path path;

    public ImageDownload(URI uri, Path path){
        this.uri = uri;
        this.path = path;
    }

    @Override
    public void run() {
        try{
            URLConnection urlConnection = uri.toURL().openConnection();
            InputStream in = urlConnection.getInputStream();
            String type = urlConnection.getContentType();
            String s = type.split("/")[1];
            type = s.split(";")[0];
            String fileName = this.generateName(type);
            File file = new File(path.toString() + "\\" + fileName +"." +  type);
            Files.copy(in, file.toPath());
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException(ex);
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public synchronized String generateName(String type){
        StringBuilder newName = new StringBuilder("installation(0)");
        ArrayList<String> names = new ArrayList<>();
        try{
            Files.walk(path,1).forEach(x->names.add(x.getFileName().toString()));
        } catch (IOException e){
            throw new IllegalArgumentException(e);
        }
        if (names.contains(newName.toString() +"." + type)){
            int i = 1;
            newName = new StringBuilder("installation(" + i + ")") ;
            while (names.contains(newName.toString() + "." + type)) {
                newName.replace(13,newName.length()-1,"" + i);
                i++;
            }
        }
        return newName.toString();
    }
}
