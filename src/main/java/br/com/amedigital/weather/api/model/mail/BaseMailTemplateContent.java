package br.com.amedigital.weather.api.model.mail;

/**
 * Base class for any email that we are sending. It only has the image host
 */
public abstract class BaseMailTemplateContent {
    /**
     * S3 host with the images
     */
    private String imagesHost;


    public BaseMailTemplateContent() {
    }

    public BaseMailTemplateContent(String imagesHost) {
        this.imagesHost = imagesHost;
    }

    public String getImagesHost() {
        return imagesHost;
    }

    public void setImagesHost(String imagesHost) {
        this.imagesHost = imagesHost;
    }
}
