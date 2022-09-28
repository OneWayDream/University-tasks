package ru.itis.cloudlab.cloudphoto.cloud;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import lombok.RequiredArgsConstructor;
import org.ini4j.Ini;
import org.springframework.stereotype.Component;
import ru.itis.cloudlab.cloudphoto.entities.AlbumPhotos;
import ru.itis.cloudlab.cloudphoto.generators.FreeMarkerPagesGenerator;
import ru.itis.cloudlab.cloudphoto.generators.PagesGenerator;
import ru.itis.cloudlab.cloudphoto.utils.ConfigUtils;
import ru.itis.cloudlab.cloudphoto.utils.GeneratedPagesUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class YandexCloudBucketWorker {

    private static final String ALBUMS_PAGE_NAME = "index";
    private static final String ALBUM_PHOTOS_PAGE_NAME = "album";
    private static final String PATH_SEPARATOR = File.separator;

    private static final String CLOUD_URL = "https://storage.yandexcloud.net/";

    private PagesGenerator pagesGenerator;
    private Ini iniConfig;
    private String bucketName;
    private AmazonS3 amazonS3;
    private TransferManager transferManager;

    public void init(){
        initIniConfig();
        initAmazonS3();
        createBucketIfNotExists();
        initTransferManager();
    }

    public void uploadFiles(String albumName, List<File> filesToUpload){
        Map<File, List<File>> multipleUploadArguments = getMultipleUploadArguments(filesToUpload);
        for (Map.Entry<File, List<File>> multipleUploadArgument: multipleUploadArguments.entrySet()){
            try{
                MultipleFileUpload upload = transferManager.uploadFileList(bucketName,
                        albumName, multipleUploadArgument.getKey(),
                        multipleUploadArgument.getValue());
                upload.waitForCompletion();
            } catch (Exception ex){
                throw new IllegalArgumentException("Can't upload images.", ex);
            }
        }
    }

    public void downloadAlbum(String albumName, File destinationFolder){
        try{
            MultipleFileDownload download = transferManager.downloadDirectory(bucketName,
                    albumName + "/", destinationFolder);
            download.waitForCompletion();
        } catch (Exception ex){
            throw new IllegalArgumentException("Can't download images.", ex);
        }
    }

    public void deleteAlbum(String albumName){
        List<DeleteObjectsRequest.KeyVersion> keys = getAlbumImageKeys(albumName);
        deleteFiles(keys);
    }

    public void deletePhoto(String albumName, String photoToDelete){
        List<DeleteObjectsRequest.KeyVersion> keys = getPhotoKeyVersion(albumName + "/" + photoToDelete);
        deleteFiles(keys);
    }

    public List<String> getAlbums(){
        return amazonS3.listObjectsV2(bucketName).getObjectSummaries().stream()
                        .map(this::getAlbumByS3ObjectSummary)
                        .distinct()
                        .filter(Objects::nonNull)
                        .toList();
    }

    public List<String> getAlbumPhotos(String albumName){
        return amazonS3.listObjectsV2(bucketName, albumName + "/").getObjectSummaries().stream()
                .map(this::getPhotoKeyByS3ObjectSummary)
                .toList();
    }

    public String createAlbumsSite(){
        initPagesGenerator();
        makeBucketPublicForReadingFiles();
        uploadPages();
        createWebSite();
        return getWebSiteUrl();
    }

    public void close(){
        if (pagesGenerator != null){
            pagesGenerator.close();
        }
    }

    private void initPagesGenerator(){
        pagesGenerator = new FreeMarkerPagesGenerator();
    }

    private void makeBucketPublicForReadingFiles(){
        amazonS3.setBucketAcl(new SetBucketAclRequest(bucketName, CannedAccessControlList.PublicRead));
    }

    private void uploadPages(){
        List<File> pageFiles = generateAlbumsContentPages();
        pageFiles.add(generateAlbumsPage());
        pageFiles.add(getErrorPage());
        uploadFiles("", pageFiles);
    }

    private List<File> generateAlbumsContentPages(){
        List<String> albums = getAlbums();
        List<File> pageFiles = new ArrayList<>();
        for (int i = 0; i < albums.size(); i++){
            String pageName = getAlbumPhotosPageName(i);
            String currentAlbum = albums.get(i);
            List<String> photos = getAlbumPhotos(currentAlbum).stream()
                    .filter(photo -> !photo.equals(""))
                    .toList();
            AlbumPhotos albumPhotos = new AlbumPhotos(photos, currentAlbum, getAlbumUrl(currentAlbum));
            pageFiles.add(pagesGenerator.generateAlbumContentPage(pageName, albumPhotos));
        }
        return pageFiles;
    }

    private String getAlbumPhotosPageName(int index){
        return ALBUM_PHOTOS_PAGE_NAME + index;
    }

    private File generateAlbumsPage(){
        return pagesGenerator.generateAlbumsPage(ALBUMS_PAGE_NAME, getAlbums());
    }

    private File getErrorPage(){
        return pagesGenerator.getErrorPage();
    }

    private void createWebSite(){
        BucketWebsiteConfiguration websiteConfiguration = new BucketWebsiteConfiguration();
        websiteConfiguration.setErrorDocument(getErrorPage().getName());
        websiteConfiguration.setIndexDocumentSuffix(ALBUMS_PAGE_NAME + GeneratedPagesUtils.PAGE_EXTENSION);
        amazonS3.setBucketWebsiteConfiguration(bucketName, websiteConfiguration);
    }

    private String getWebSiteUrl(){
        return "https://" + bucketName + ".website.yandexcloud.net";
    }

    private String getAlbumUrl(String albumName){
        return CLOUD_URL + bucketName + "/" + albumName + "/";
    }

    private void initIniConfig(){
        iniConfig = ConfigUtils.getInstance().getConfig();
    }

    private String getAlbumByS3ObjectSummary(S3ObjectSummary s3ObjectSummary){
        String fileKey = s3ObjectSummary.getKey();
        int prefixEnd = fileKey.lastIndexOf("/");
        String result = null;
        if (prefixEnd != -1){
            result = fileKey.substring(0, prefixEnd);
        }
        return result;
    }

    private String getPhotoKeyByS3ObjectSummary(S3ObjectSummary s3ObjectSummary){
        String fileKey = s3ObjectSummary.getKey();
        return fileKey.substring(fileKey.lastIndexOf("/") + 1);
    }

    private void deleteFiles(List<DeleteObjectsRequest.KeyVersion> fileKeys){
        try{
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName).withKeys(fileKeys);
            amazonS3.deleteObjects(deleteObjectsRequest);
        } catch (Exception ex){
            throw new IllegalArgumentException("Can't delete album.", ex);
        }
    }

    private List<DeleteObjectsRequest.KeyVersion> getPhotoKeyVersion(String photoKey){
        List<DeleteObjectsRequest.KeyVersion> photoKeyVersion =  amazonS3.listObjectsV2(bucketName, photoKey)
                .getObjectSummaries().stream()
                .map(s3ObjectSummary -> new DeleteObjectsRequest.KeyVersion(s3ObjectSummary.getKey()))
                .toList();
        if (photoKeyVersion.isEmpty()){
            throw new IllegalArgumentException("This photo doesn't exist");
        }
        return photoKeyVersion;
    }

    private void initTransferManager(){
        this.transferManager = TransferManagerBuilder.standard().withS3Client(amazonS3).build();
    }

    private Map<File, List<File>> getMultipleUploadArguments(List<File> filesToUpload){
        return filesToUpload.stream()
                .collect(Collectors.groupingBy(this::getDirectoryPathForFile));
    }

    private File getDirectoryPathForFile(File file){
        String filePath = file.getAbsolutePath();
        String directoryPath = filePath.substring(0, filePath.lastIndexOf(PATH_SEPARATOR));
        return new File(directoryPath);
    }

    private List<DeleteObjectsRequest.KeyVersion> getAlbumImageKeys(String albumName){
        List<DeleteObjectsRequest.KeyVersion> albumImageKey =
                amazonS3.listObjectsV2(bucketName, albumName + "/").getObjectSummaries().stream()
                .map(s3ObjectSummary -> new DeleteObjectsRequest.KeyVersion(s3ObjectSummary.getKey()))
                .toList();
        if (albumImageKey.isEmpty()){
            throw new IllegalArgumentException("This album doesn't exist");
        }
        return albumImageKey;
    }

    private void initAmazonS3(){
        AWSCredentials credentials = getCredentials();
        amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(
                        new AmazonS3ClientBuilder.EndpointConfiguration(
                                getServiceEndpoint(), getSigningRegion()
                        )
                )
                .build();
    }

    private BasicAWSCredentials getCredentials(){
        String awsAccessKeyId = iniConfig.get("DEFAULT", "aws_access_key_id");
        String awsSecretAccessKey = iniConfig.get("DEFAULT", "aws_secret_access_key");
        return new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
    }

    private String getServiceEndpoint(){
        return iniConfig.get("DEFAULT", "endpoint_url");
    }

    private String getSigningRegion(){
        return iniConfig.get("DEFAULT", "region");
    }

    private void createBucketIfNotExists(){
        initBucketName();
        boolean doesBucketExists = checkIfBucketExists(bucketName);
        if (!doesBucketExists){
            createBucket(bucketName);
        }
    }

    private void initBucketName(){
        bucketName = iniConfig.get("DEFAULT", "bucket");
    }


    private boolean checkIfBucketExists(String bucketName){
        return amazonS3.doesBucketExistV2(bucketName);
    }

    private void createBucket(String bucketName){
        amazonS3.createBucket(bucketName);
    }

}
