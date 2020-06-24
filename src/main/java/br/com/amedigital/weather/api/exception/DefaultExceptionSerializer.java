package br.com.amedigital.weather.api.exception;


import com.fasterxml.jackson.annotation.JsonProperty;

public class DefaultExceptionSerializer {

    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

    public DefaultExceptionSerializer() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
