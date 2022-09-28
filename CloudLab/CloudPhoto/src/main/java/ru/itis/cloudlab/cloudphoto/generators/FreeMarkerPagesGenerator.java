package ru.itis.cloudlab.cloudphoto.generators;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import ru.itis.cloudlab.cloudphoto.entities.AlbumPhotos;
import ru.itis.cloudlab.cloudphoto.utils.GeneratedPagesUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreeMarkerPagesGenerator implements PagesGenerator {

    private static final String BASE_PACKAGE_PATH = "templates";
    private static final String ALBUMS_PAGE_TEMPLATE = "albums_page.ftlh";
    private static final String ALBUM_PHOTOS_PAGE_TEMPLATE = "album_photos_page.ftlh";

    private static final String ERROR_PAGE = "error.ftlh";

    private final Configuration configuration;
    private final GeneratedPagesUtils utils;

    public FreeMarkerPagesGenerator() {
        configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setClassLoaderForTemplateLoading(getClass().getClassLoader(), BASE_PACKAGE_PATH);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setOutputEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
        utils = GeneratedPagesUtils.getInstance();
    }

    @Override
    public File generateAlbumsPage(String fileName, List<String> albums) {
        Map<String, Object> freemarkerDataModel = new HashMap<>();
        freemarkerDataModel.put("albums", albums);
        return getFile(fileName, freemarkerDataModel, ALBUMS_PAGE_TEMPLATE);
    }

    @Override
    public File generateAlbumContentPage(String fileName, AlbumPhotos photos) {
        Map<String, Object> freemarkerDataModel = new HashMap<>();
        freemarkerDataModel.put("photo_content", photos);
        return getFile(fileName, freemarkerDataModel, ALBUM_PHOTOS_PAGE_TEMPLATE);
    }

    @Override
    public File getErrorPage() {
        return getFile("error", new HashMap<>(), ERROR_PAGE);
    }

    @Override
    public void close() {
        utils.deletePagesFolder();
    }

    private File getFile(String fileName, Map<String, Object> freemarkerDataModel, String albumPhotosPageTemplate) {
        File generatedPageFile = utils.createPageFile(fileName);
        try{
            Template template = configuration.getTemplate(albumPhotosPageTemplate);
            Writer generatedPageWriter = new FileWriter(generatedPageFile, StandardCharsets.UTF_8);
            template.process(freemarkerDataModel, generatedPageWriter);
        } catch (IOException | TemplateException ex) {
            throw new IllegalArgumentException("Can't generate page.", ex);
        }
        return generatedPageFile;
    }

}
