package br.com.amedigital.weather.api.exception;

import br.com.amedigital.weather.api.model.ErrorMessages;

public class ValidationException extends ServiceException {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public ValidationException(final Exception aCause) {
		super(aCause);
	}

	public ValidationException(final ErrorMessages error, final Exception aCause) {
		super(error, aCause);
	}

	public ValidationException(final ErrorMessages error, final String... someParams) {
		super(error, someParams);
	}

	public ValidationException(final ErrorMessages error) {
		super(error);
	}

	public ValidationException(String anAdditionalMessage, ErrorMessages error, final String... someParams) {
		super( anAdditionalMessage, error , someParams);
	}

}