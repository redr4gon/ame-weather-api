package br.com.amedigital.weather.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

public class ErrorResponse {

    public static final String DEFAULT_ERROR_DESCRIPTION = "Ocorreu um erro desconhecido durante o processamento!";
    public static final String DEFAULT_ERROR_CODE = "ServiceException";

    @JsonProperty("error")
    private final String errorCode;

    @JsonProperty("error_description")
    private final String errorDescription;

    public ErrorResponse(String errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public ErrorResponse() {
        errorCode = DEFAULT_ERROR_CODE;
        errorDescription = DEFAULT_ERROR_DESCRIPTION;
    }

    public String getErrorCode() {
        return StringUtils.defaultString(errorCode, DEFAULT_ERROR_CODE);
    }

    public String getErrorDescription() {
        return StringUtils.defaultString(errorDescription, DEFAULT_ERROR_DESCRIPTION);
    }

}
