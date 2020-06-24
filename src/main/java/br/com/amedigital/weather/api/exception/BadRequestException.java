package br.com.amedigital.weather.api.exception;


import br.com.amedigital.weather.api.model.ErrorMessages;

public class BadRequestException extends ServiceException {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public BadRequestException(final Exception aCause) {
		super(aCause);
	}

	public BadRequestException(final ErrorMessages error, final Exception aCause) {
		super(error, aCause);
	}

	public BadRequestException(final ErrorMessages error, final String... someParams) {
		super(error, someParams);
	}

	public BadRequestException(final ErrorMessages error) {
		super(error);
	}

}