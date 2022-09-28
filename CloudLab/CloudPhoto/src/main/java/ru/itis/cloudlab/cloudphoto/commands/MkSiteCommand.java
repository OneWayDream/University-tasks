package ru.itis.cloudlab.cloudphoto.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.cloudlab.cloudphoto.cloud.YandexCloudBucketWorker;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MkSiteCommand implements ConsoleCommand {

    private final YandexCloudBucketWorker yandexCloudBucketWorker;

    @Override
    public void execute() {
        initYandexCloudBucketWorker();
        String webSiteUrl = yandexCloudBucketWorker.createAlbumsSite();
        System.out.println(webSiteUrl);
        closeYandexCloudBucketWorker();
    }

    private void initYandexCloudBucketWorker(){
        yandexCloudBucketWorker.init();
    }

    private void closeYandexCloudBucketWorker(){
        yandexCloudBucketWorker.close();
    }

    @Override
    public Command getCommandType() {
        return Command.MK_SITE;
    }

    @Override
    public void setArguments(Map<String, String> arguments) {

    }

}
