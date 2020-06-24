package br.com.amedigital.weather.api.exception;

import br.com.amedigital.weather.api.model.ErrorMessages;

public class WalletException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public WalletException(final Exception aCause) {
        super(ErrorMessages.GENERIC_EXCEPTION, aCause);
    }

    public WalletException(final ErrorMessages error, final Exception aCause) {
        super(error, aCause);
    }

    public WalletException(final ErrorMessages error, final String... someParams) {
        super(error, someParams);
    }

    public WalletException(final ErrorMessages error) {
        super(error);
    }

    public WalletException(String anAdditionalMessage, ErrorMessages error, final String... someParams) {
        super(anAdditionalMessage, error, someParams);
    }

    public WalletException() {
        super(ErrorMessages.GENERIC_EXCEPTION);
    }

}
