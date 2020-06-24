package br.com.amedigital.weather.api.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;

@JsonSerialize(using = BaseExceptionSerializer.class)
public class BaseRuntimeException extends RuntimeException {

    private int httpStatus;
    private String errorCode;
    private Map<String, String> fields;

    public BaseRuntimeException() {
        this.httpStatus = 500;
    }

    public BaseRuntimeException(int httpStatus, String errorCode, String description) {
        super(description);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public BaseRuntimeException(int httpStatus, String description, Throwable cause) {
        super(description, cause);
        this.httpStatus = httpStatus;
    }

    public BaseRuntimeException(int httpStatus, String errorCode, String description, Throwable cause) {
        super(description, cause);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public BaseRuntimeException(int httpStatus, String errorCode, String description, Map<String, String> fields, Throwable cause) {
        super(description, cause);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.fields = fields;
    }

    public BaseRuntimeException(String description, Throwable cause) {
        super(description, cause);
    }

    /**
     * Descrição do erro.
     * <p>
     * Ex.: CPF Inválido
     */
    public String getErrorDescription() {
        return getMessage();
    }

    /**
     * Status HTTP a ser retornado na requisição
     * Ex.: 400
     */
    public int getHttpStatus() {
        return httpStatus;
    }

    /**
     * Código de erro da API
     * <p>
     * Ex. invalid_request
     *
     * @return String
     */
    public String getErrorCode() {
        return errorCode;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}
