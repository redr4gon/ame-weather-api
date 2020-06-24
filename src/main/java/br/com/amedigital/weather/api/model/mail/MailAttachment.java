package br.com.amedigital.weather.api.model.mail;

/**
 * Holds the email attachments
 */
public class MailAttachment {

    /**
     * Attachment file.;
     */
    private byte[] file;
    /**
     * Attachment name
     */
    private String name;
    /**
     * attachment type
     */
    private String type;
    /**
     * Attachment description (optional)
     */
    private String description;

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
