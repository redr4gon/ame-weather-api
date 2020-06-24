package br.com.amedigital.weather.api.config.webclient;

import br.com.amedigital.weather.api.model.ErrorMessages;
import br.com.amedigital.weather.api.exception.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * Exceptions throw when we have a issue in the partner side
 *
 * @author Flavio Solci
 */
public class ClientException extends ServiceException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private HttpStatus statusCode;
    private String errorMessage;

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {return errorMessage;}

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public ClientException(final Exception aCause) {
        super(ErrorMessages.GENERIC_EXCEPTION, aCause);
    }

    public ClientException(final ErrorMessages error, final Exception aCause) {
        super(error, aCause);
    }

    public ClientException(String anAdditionalMessage, ErrorMessages error, final String... someParams) {
        super(anAdditionalMessage, error, someParams);
    }

    public ClientException(final ErrorMessages error, final String... someParams) {
        super(error, someParams);
    }

    public ClientException(final ErrorMessages error, final HttpStatus statusCode, final String errorMessage) {
        super(error);
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public ClientException(final ErrorMessages error) {
        super(error);
    }

}
