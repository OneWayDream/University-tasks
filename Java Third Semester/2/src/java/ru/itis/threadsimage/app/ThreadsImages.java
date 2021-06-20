package ru.itis.threadsimage.app;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.StringConverter;
import com.beust.jcommander.converters.IParameterSplitter;

import ru.itis.threadsimage.utils.ImageDownload;
import ru.itis.threadsimage.utils.MyThreadsPool;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import java.util.ArrayList;
import java.util.List;

@Parameters(separators = "=")
public class ThreadsImages {

    @Parameter(names = {"--mode"})
    public String mode;

    @Parameter(names = {"--count"})
    public int num;

    @Parameter(names = {"--files"} , converter = StringConverter.class, splitter = SemiColonSplitter.class)
    public List<String> uris;

    @Parameter(names = {"--folder"})
    public String pathString;

    protected static ArrayList<String> names = new ArrayList<>();

    public void run() {
        MyThreadsPool myThreadsPool = new MyThreadsPool(num);
        Path path = Paths.get(pathString);
        try{
            Files.walk(path,1).forEach(x->names.add(x.getFileName().toString()));
        } catch (IOException e){
            throw new IllegalArgumentException(e);
        }

        names.remove(0);

        for (int i = 0; i<uris.size(); i++){
			try{
				myThreadsPool.submit(new ImageDownload(new URI(uris.get(i)), path));
			} catch (URISyntaxException ex){
                throw new IllegalArgumentException(ex);
            }
        }
    }

    public static synchronized String getName(String type){
        StringBuilder newName = new StringBuilder("installation(0)");
        if (names.contains(newName.toString() +"." + type)){
            int i = 1;
            newName = new StringBuilder("installation(" + i + ")") ;
            while (names.contains(newName.toString() + "." + type)) {
                newName.replace(13,newName.length()-1,"" + i);
                i++;
            }
        }
        names.add(newName.toString() +"." + type);
        return newName.toString();
    }

    static class SemiColonSplitter implements IParameterSplitter {
        public List<String> split(String value) {
            return Arrays.asList(value.split(";"));
        }
    }
}
