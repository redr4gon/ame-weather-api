package br.com.amedigital.weather.api.exception;

import br.com.amedigital.weather.api.controller.response.ErrorResponse;
import br.com.amedigital.weather.api.model.ErrorMessages;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOG = getLogger(ControllerExceptionHandler.class);

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServerWebInputException.class)
    public ErrorResponse handleInvalidServerInputException(final ServerWebInputException ex) {
        LOG.error("ServerWebInputException", ex);
        return new ErrorResponse(ex.getClass().getSimpleName(), ex.getReason());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WalletException.class)
    public ErrorResponse handleWalletException(final WalletException ex) {
        LOG.error("WalletException", ex);
        return new ErrorResponse(ex.getClass().getSimpleName(), ex.getLocalizedMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorResponse handleBadRequestException(final BadRequestException ex) {
        LOG.error("BadRequestException", ex);
        return new ErrorResponse(ex.getClass().getSimpleName(), ex.getLocalizedMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFoundException(final NotFoundException ex) {
        LOG.error("NotFoundException", ex);
        return new ErrorResponse(ex.getClass().getSimpleName(), ex.getLocalizedMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(PartnerException.class)
    public ErrorResponse handlePartnerException(final PartnerException ex) {
        LOG.error("PartnerException", ex);
        return new ErrorResponse(ex.getClass().getSimpleName(), ex.getLocalizedMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ErrorResponse handleValidationException(final ValidationException ex) {
        LOG.error("ValidationException", ex);
        return new ErrorResponse(ex.getClass().getSimpleName(), ex.getLocalizedMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public ErrorResponse handleValidationException(final WebExchangeBindException ex) {
        LOG.error("ValidationException", ex);

        LOG.debug("errors: {}", ex.getFieldErrors());
        List<String> errors = new ArrayList<>();
        ex.getFieldErrors().forEach(e -> errors.add(e.getField() + " - " + e.getDefaultMessage() + " / "));

        LOG.debug("handled errors::{}", errors);

        return new ErrorResponse(ex.getClass().getSimpleName(), ErrorMessages.GENERIC_EXCEPTION.getMessage() + " " + errors);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ErrorResponse handleValidationException(final ForbiddenException ex) {
        LOG.error("ForbiddenException", ex);
        return new ErrorResponse(ex.getClass().getSimpleName(), ex.getLocalizedMessage());
    }

    /**
     * Handler for any generic exception that was not caught by the other handlers
     *
     * @param ex Exception
     * @return Generic error message
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericException(final Exception ex) {
        LOG.error("Exception", ex);
        return new ErrorResponse();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handle(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        String errorMessage;
        if (!violations.isEmpty()) {
            errorMessage = violations.stream().map(violation -> " " + violation.getMessage()).collect(Collectors.toList()).toString();
        } else {
            errorMessage = "ConstraintViolationException occured.";
        }
        return new ErrorResponse(ex.getClass().getSimpleName(), errorMessage);
    }
}
