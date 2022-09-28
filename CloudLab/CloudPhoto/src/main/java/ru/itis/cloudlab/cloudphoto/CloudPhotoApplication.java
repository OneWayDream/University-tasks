package ru.itis.cloudlab.cloudphoto;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class CloudPhotoApplication implements CommandLineRunner {

    private final CloudPhotoConsoleApplication consoleApplication;

    public static void main(String[] args) {
        SpringApplication.run(CloudPhotoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String commandLine = mapArgsToCommandLine(args);
        consoleApplication.start(commandLine);
    }

    private String mapArgsToCommandLine(String[] args){
        return String.join(" ", args);
    }

}
