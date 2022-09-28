package ru.itis.cloudlab.cloudphoto.commands;

import lombok.RequiredArgsConstructor;
import org.ini4j.Ini;
import org.springframework.stereotype.Component;
import ru.itis.cloudlab.cloudphoto.CloudPhotoConsoleApplication;
import ru.itis.cloudlab.cloudphoto.cloud.YandexCloudBucketWorker;
import ru.itis.cloudlab.cloudphoto.utils.ConfigUtils;

import java.util.Map;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class InitCommand implements ConsoleCommand {

    private final Scanner scanner = CloudPhotoConsoleApplication.getCommandLineScanner();
    private final YandexCloudBucketWorker yandexCloudBucketWorker;
    private final ConfigUtils configUtils = ConfigUtils.getInstance();

    private Ini properties;

    @Override
    public void execute() {
        initProperties();
        askForProperties();
        initDefaultProperties();
        saveConfig();
        checkParameters();
    }

    private void initProperties(){
        properties = new Ini();
    }

    private void askForProperties(){
        askForAwsAccessKeyId();
        askForAwsSecretAccessKey();
        askForBucket();
    }

    private void askForAwsAccessKeyId(){
        System.out.println("Enter your aws_access_key_id: ");
        String awsAccessKeyId = scanner.nextLine().trim();
        properties.put("DEFAULT", "aws_access_key_id", awsAccessKeyId);
    }

    private void askForAwsSecretAccessKey(){
        System.out.println("Enter your aws_secret_access_key: ");
        String awsSecretAccessKey = scanner.nextLine().trim();
        properties.put("DEFAULT", "aws_secret_access_key", awsSecretAccessKey);
    }

    private void askForBucket(){
        System.out.println("Enter your bucket: ");
        String bucket = scanner.nextLine().trim();
        properties.put("DEFAULT", "bucket", bucket);
    }

    private void initDefaultProperties(){
        properties.put("DEFAULT", "region", "ru-central1");
        properties.put("DEFAULT", "endpoint_url", "https://storage.yandexcloud.net");
    }

    private void checkParameters(){
        yandexCloudBucketWorker.init();
    }

    private void saveConfig(){
        configUtils.saveConfig(properties);
    }
    @Override
    public Command getCommandType() {
        return Command.INIT;
    }

    @Override
    public void setArguments(Map<String, String> arguments) {
        //ignore lol no arguments for exit command
    }

}
