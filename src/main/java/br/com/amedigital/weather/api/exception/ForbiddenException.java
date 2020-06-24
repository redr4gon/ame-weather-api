package br.com.amedigital.weather.api.exception;


import br.com.amedigital.weather.api.model.ErrorMessages;

public class ForbiddenException extends ServiceException {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public ForbiddenException(final Exception aCause) {
		super(aCause);
	}

	public ForbiddenException(final ErrorMessages error, final Exception aCause) {
		super(error, aCause);
	}

	public ForbiddenException(final ErrorMessages error, final String... someParams) {
		super(error, someParams);
	}

	public ForbiddenException(final ErrorMessages error) {
		super(error);
	}

}