package br.com.amedigital.weather.api.exception;


import br.com.amedigital.weather.api.model.ErrorMessages;

public class NotFoundException extends ServiceException {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public NotFoundException(final Exception aCause) {
		super(aCause);
	}

	public NotFoundException(final ErrorMessages error, final Exception aCause) {
		super(error, aCause);
	}

	public NotFoundException(final ErrorMessages error, final String... someParams) {
		super(error, someParams);
	}

	public NotFoundException(final ErrorMessages error) {
		super(error);
	}

	public NotFoundException(String anAdditionalMessage, ErrorMessages error, final String... someParams) {
		super( anAdditionalMessage, error , someParams);
	}

}
