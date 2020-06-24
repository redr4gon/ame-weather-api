package br.com.amedigital.weather.api.exception;

import br.com.amedigital.weather.api.model.ErrorMessages;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

public class ServiceRuntimeException extends RuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = -6776040048618983504L;

    /** Message Parameters */
    private final String[] params;

    /** Error Message */
    private final ErrorMessages errorFromRepo;

    /** In case we need save any additional information, like the JSON returned by a failed call */

    private final String additionalMessage;

    public String getAdditionalMessage() {
        return additionalMessage;
    }

    /**
     * Generic exception. Avoid using that, try always sending a proper message
     */
    public ServiceRuntimeException() {
        errorFromRepo = ErrorMessages.GENERIC_EXCEPTION;
        params = null;
        additionalMessage = null;
    }

    /**
     * Constructor
     *
     * @param error Error Message from repository
     */
    public ServiceRuntimeException(final ErrorMessages error) {
        errorFromRepo = error;
        params = null;
        additionalMessage = null;

    }

    /**
     * Constructor
     *
     * @param error      Error Message from repository
     * @param someParams Parameters
     */
    public ServiceRuntimeException(final ErrorMessages error, final String... someParams) {
        errorFromRepo = error;
        params = someParams;
        additionalMessage = null;
    }

    /**
     * Constructor
     *
     * @param error  Error Message from repository
     * @param aCause an Exception
     */
    public ServiceRuntimeException(final ErrorMessages error, final Exception aCause) {
        super(aCause);
        errorFromRepo = error;
        params = null;
        additionalMessage = null;
    }

    /**
     * Constructor
     *
     * @param aCause any java Exception
     */
    public ServiceRuntimeException(final Exception aCause) {
        super(aCause);
        errorFromRepo = null;
        params = null;
        additionalMessage = null;
    }

    /**
     * Constructor
     *
     * @param anAdditionalMessage Any additional information that need be logged, such as the response JSON from a failed call
     * @param error               Error Message from repository
     * @param someParams          Any additional parameter for the message
     */
    public ServiceRuntimeException(final String anAdditionalMessage, final ErrorMessages error, final String... someParams) {
        errorFromRepo = error;
        params = someParams;
        additionalMessage = anAdditionalMessage;
    }

    /**
     * Formats a string using the parameters
     *
     * @param errorMsg   The error message text
     * @return Formatted message
     */
    private String getFormattedMsg(final String errorMsg) {
        String formatMsg = errorMsg;
        if ((params != null) && (params.length > 0)) {
            final MessageFormat fmt = new MessageFormat(errorMsg);
            formatMsg = fmt.format(params);
        }
        return formatMsg;
    }

    /**
     * Gets the message from repository and formats it
     *
     * @param error      Error from repository
     * @return Formatted message
     */
    private String getFormattedMsg(final ErrorMessages error) {
        return getFormattedMsg(error.getMessage());

    }

    /**
     * @return Message with additional information.
     */
    public String getMessageForLog() {
        String defaultEx = String.format("Exception: %s. ", getLocalizedMessage());
        if (StringUtils.isNotEmpty(getAdditionalMessage())) {
            defaultEx = String.format("%s.  Additional Message:  %s", defaultEx, getAdditionalMessage());
        }

        return defaultEx;
    }

    /**
     * Returns the formatted message. If not parameters returns the message as it is
     *
     * @return Formatted Message
     */
    private String getFormattedMsg() {
        if (errorFromRepo == null) {
            return getFormattedMsg(getCause() != null ? getCause().getLocalizedMessage()
                    : "Ocorreu um erro desconhecido durante o processamento!");
        }
        return getFormattedMsg(errorFromRepo);

    }

    @Override
    public String getMessage() {
        return getFormattedMsg();
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    /**
     * Error message
     *
     * @return GenericErrorMessages
     */
    public ErrorMessages getErrorFromRepo() {
        return errorFromRepo;
    }

}
