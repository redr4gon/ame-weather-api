package br.com.amedigital.weather.api.config.security;

import br.com.amedigital.weather.api.controller.response.ErrorResponse;
import br.com.amedigital.weather.api.exception.BaseRuntimeException;
import br.com.amedigital.weather.api.exception.NotFoundException;
import br.com.amedigital.weather.api.model.ErrorMessages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class ExceptionHandler implements WebExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandler.class);
    private final ObjectMapper mapper;
    private final DataBufferFactory dataBufferFactory;

    @Autowired
    public ExceptionHandler(ObjectMapper mapper, DataBufferFactory dataBufferFactory) {
        this.mapper = mapper;
        this.dataBufferFactory = dataBufferFactory;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ExceptionParserInterface parser = ame -> {
            exchange.getResponse().setStatusCode(HttpStatus.resolve(ame.getHttpStatus()));
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            var body = Mono.just(dataBufferFactory.wrap(mapper.writeValueAsBytes(ame)));
            return exchange.getResponse().writeWith(body);
        };

        try {

            if (ex instanceof BaseRuntimeException) {
                return handleBaseRuntimeException(parser, (BaseRuntimeException) ex);
            }

            if (ex instanceof ResponseStatusException) {
                return handleResourceNotFoundException(exchange, (ResponseStatusException) ex);
            }

            LOG.error("Ocorreu a seguinte exception na chamada [{}] com user-agent [{}]: [{}]", exchange.getRequest().getPath().value(), exchange.getRequest().getHeaders().getFirst("user-agent"), ex);
            return handleGenericException(exchange);

        } catch (JsonProcessingException e) {
            LOG.error("Não foi possível mapear a exceção na chamada [{}]", exchange.getRequest().getPath().value());
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        } finally {
            LOG.error("Ocorreu a seguinte exception na chamada [{}] com user-agent [{}]: [{}]", exchange.getRequest().getPath().value(), exchange.getRequest().getHeaders().getFirst("user-agent"), ex);
        }
    }

    /** handle runtime error */
    private Mono<Void> handleBaseRuntimeException(ExceptionParserInterface parser, BaseRuntimeException ex) throws JsonProcessingException {
        return Mono.from(parser.parse(ex));
    }

    /** handle 404 error */
    private Mono<Void> handleResourceNotFoundException(ServerWebExchange exchange, ResponseStatusException ex) throws JsonProcessingException {
        exchange.getResponse().setStatusCode(ex.getStatus());
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ErrorResponse errorResponse = new ErrorResponse(NotFoundException.class.getSimpleName(), ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION.getMessage());

        return Mono.from(writeResponse(exchange, errorResponse));
    }

    /** handle any exception as a server error */
    private Mono<Void> handleGenericException(ServerWebExchange exchange) throws JsonProcessingException {
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ErrorResponse errorResponse = new ErrorResponse(BaseRuntimeException.class.getSimpleName(), ErrorMessages.GENERIC_EXCEPTION.getMessage());

        return Mono.from(writeResponse(exchange, errorResponse));
    }

    /**
     * Write the given error response in the server response
     */
    private Mono<Void> writeResponse(ServerWebExchange exchange, ErrorResponse errorResponse) throws JsonProcessingException {
        Mono<DataBuffer> body = Mono.just(dataBufferFactory.wrap(mapper.writeValueAsBytes(errorResponse)));
        return exchange.getResponse().writeWith(body);
    }
}
