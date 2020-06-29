package br.com.amedigital.weather.api.config.webclient;

import br.com.amedigital.weather.api.model.ErrorMessages;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class BaseWebClient {

    private static final Logger LOG = getLogger(BaseWebClient.class);

    // used to access the partner
    protected String url;

    protected final WebClient webClient;

    @Autowired
    public BaseWebClient(WebClient webClient, String url) {
        this.url = url;
        this.webClient = webClient;
    }

    /**
     * @param method HTTP Method [GET, POST, PUT...]
     * @param uri    Url to call client
     * @param clazz  Class to Mono be converted
     * @param <C>    Generic class
     * @return Mono
     */
    protected <C> Mono<C> handleGenericMono(HttpMethod method, UriComponents uri, Class<C> clazz, String mediaType) {
        requiredFields(uri, clazz);
        return retrieveMono(requestBuilder(uri.toUri(), method, mediaType), clazz);
    }

    protected <C> Flux<C> handleGenericFlux(HttpMethod method, UriComponents uri, Class<C> clazz, String mediaType) {
        requiredFields(uri, clazz);
        return retrieveFlux(requestBuilder(uri.toUri(), method, mediaType), clazz);
    }

    protected UriComponentsBuilder urlBuilder() {
        try {
            URI uri = new URI(url);
            return UriComponentsBuilder.newInstance().uri(uri);
        } catch (URISyntaxException e) {
            LOG.error("=== Error building url to webclient ===", e);
            return null;
        }
    }

    private <C> Mono<C> retrieveMono(WebClient.RequestBodySpec request, Class<C> clazz) {
        return request
                .retrieve()
                .onStatus(HttpStatus::isError, this::getErrorResponse)
                .bodyToMono(clazz)
                .onErrorResume(Mono::error);
    }

    private <C> Flux<C> retrieveFlux(WebClient.RequestBodySpec request, Class<C> clazz) {
        return request
                .retrieve()
                .onStatus(HttpStatus::isError, this::getErrorResponse)
                .bodyToFlux(clazz)
                .onErrorResume(Mono::error);
    }

    @NotNull
    private Mono<ClientException> getErrorResponse(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class).map(responseBody -> new ClientException(ErrorMessages.GENERIC_EXCEPTION, clientResponse.statusCode(), responseBody));
    }

    private WebClient.RequestBodySpec requestBuilder(URI uri, HttpMethod method, String mediaType) {
        return webClient
                .method(method)
                .uri(uri)
                .header(HttpHeaders.ACCEPT, mediaType);
    }

    private <C> void requiredFields(UriComponents uri, Class<C> clazz) {
        Objects.requireNonNull(uri, "Missing Uri Property Exception");
        Objects.requireNonNull(clazz, "Missing Clazz Property Exception");
    }

}
