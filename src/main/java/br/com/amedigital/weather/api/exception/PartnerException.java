package br.com.amedigital.weather.api.exception;

import br.com.amedigital.weather.api.model.ErrorMessages;

/**
 * Exceptions throw when we have a issue in the partner side
 *
 * @author Flavio Solci
 */
public class PartnerException extends ServiceException {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    public PartnerException(final Exception aCause) {
        super(ErrorMessages.GENERIC_EXCEPTION, aCause);
    }

    public PartnerException(final ErrorMessages error, final Exception aCause) {
        super(error, aCause);
    }

    public PartnerException(String anAdditionalMessage, ErrorMessages error, final String... someParams) {
        super( anAdditionalMessage, error , someParams);
    }

    public PartnerException(final ErrorMessages error, final String... someParams) {
        super(error, someParams);
    }

    public PartnerException(final ErrorMessages error) {
        super(error);
    }

}
