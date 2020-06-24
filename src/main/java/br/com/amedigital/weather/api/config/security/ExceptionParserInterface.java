package br.com.amedigital.weather.api.config.security;

import br.com.amedigital.weather.api.exception.BaseRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.reactivestreams.Publisher;

public interface ExceptionParserInterface {

    Publisher<Void> parse(BaseRuntimeException ex) throws JsonProcessingException;

}