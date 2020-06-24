package br.com.amedigital.weather.api.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ResourceBundle;

/**
 * Top class of all the error messages. Any error message must extend this
 *
 * @author Flavio Solci
 */
public class ErrorMessages {

    /**
     * Default resource bundle name
     */
    public static final String DEFAULT_RESOURCE = "messages.errors.resources";

    public static final ErrorMessages GENERIC_EXCEPTION = new ErrorMessages("generic");

    public static final ErrorMessages GENERIC_BAD_REQUEST_EXCEPTION = new ErrorMessages("generic.badRequest");

    public static final ErrorMessages GENERIC_UNAUTHORIZED_EXCEPTION = new ErrorMessages("generic.unauthorized");

    public static final ErrorMessages GENERIC_FORBIDDEN_EXCEPTION = new ErrorMessages("generic.forbidden");

    public static final ErrorMessages GENERIC_NOT_FOUND_EXCEPTION = new ErrorMessages("generic.notFound");

    /**
     * Unique message id
     */
    private final int messageID;
    /**
     * Message Key
     */
    private final String key;

    /**
     * Constructor
     *
     * @param aMessageID Unique message id
     * @param aKey       Message Key
     */
    protected ErrorMessages(final int aMessageID, final String aKey) {
        messageID = aMessageID;
        key = aKey;
    }

    /**
     * Constructor with default ID zero
     *
     * @param aKey Message Key
     */
    protected ErrorMessages(final String aKey) {
        messageID = 0;
        key = aKey;
    }

    // ====================================
    // == RESOURCE BUNDLE KEY
    // ====================================

    /**
     * @return Message Key
     */
    public String getKey() {
        return key;
    }

    // ====================================
    // == MESSAGE ID
    // ====================================

    /**
     * @return Unique Message ID
     */
    public String getMessageID() {
        return StringUtils.leftPad(String.valueOf(messageID), 3, "0");
    }

    // ====================================
    // == FORMATTED MESSAGE
    // ====================================

    /**
     * @return Formatted message
     */
    public String getMessage() {
        return StringEscapeUtils.unescapeJava(tryGetMessageFromBundle());
    }

    /**
     * @return first try get the message from subclass resource (if any), in case it
     * does not exists try the default. If also does not exists an exception
     * will be throw
     */
    protected String tryGetMessageFromBundle() {
        if (getResource().containsKey(getKey())) {
            return getResource().getString(getKey());
        }
        return ResourceBundle.getBundle(DEFAULT_RESOURCE).getString(getKey());
    }

    // ====================================
    // == RESOURCE BUNDLE
    // ====================================

    /**
     * @return Resource that contains the messages
     */
    public ResourceBundle getResource() {
        return ResourceBundle.getBundle(DEFAULT_RESOURCE);
    }

}