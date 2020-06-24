package br.com.amedigital.weather.api.model.mail;

/**
 * Configuration for the email
 */
public class MailConfig {

    /** email host */
    private String host;

    /** email username */
    private String username;

    /** email password */
    private String password;

    /** email sender */
    private String sender;

    /** email port */
    private Integer port;

    public MailConfig() {
    }

    public MailConfig(String host, String username, String password, String sender, Integer port) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.sender = sender;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSender() {
        return sender;
    }

    public Integer getPort() {
        return port;
    }
}
