package br.com.amedigital.weather.api.service.partner;

import br.com.amedigital.weather.api.config.webclient.BaseWebClient;
import br.com.amedigital.weather.api.model.partner.response.INPECityResponse;
import br.com.amedigital.weather.api.model.partner.response.INPEWeatherCityResponse;
import com.newrelic.api.agent.Trace;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class INPEClientService extends BaseWebClient {

    private static final Logger LOG = getLogger(INPEClientService.class);

    public static final String CITY_WEATHER = "cidade/#/previsao.xml";
    public static final String CITY_LIST = "listaCidades";

    @Autowired
    public INPEClientService(final WebClient webClient, @Value("${partner.url}") final String url) {
        super(webClient, url);
    }

    @Trace(dispatcher = true)
    public Mono<INPEWeatherCityResponse> findWeatherToCity(Integer cityCode) {
        LOG.debug("==== Find weather to city ====");

        return handleGenericMono(HttpMethod.GET, urlWeather(cityCode), INPEWeatherCityResponse.class, MediaType.APPLICATION_XML_VALUE)
                .doOnError(throwable -> LOG.error("=== Error finding weather to city ===", throwable));
    }

    @Trace(dispatcher = true)
    public Mono<INPEWeatherCityResponse> findWeatherToCityToNextWeek(Integer cityCode) {
        LOG.debug("==== Find weather to city to next week ====");

        return handleGenericMono(HttpMethod.GET, urlWeatherNextWeek(cityCode), INPEWeatherCityResponse.class, MediaType.APPLICATION_XML_VALUE)
                .map(city -> {
                    city.setCode(cityCode);
                    return city;
                })
                .doOnError(throwable -> LOG.error("=== Error finding weather to city to next week ===", throwable));
    }

    @Trace(dispatcher = true)
    public Flux<INPECityResponse> findCityByName(String cityName) {
        LOG.debug("==== Find city by name ====");

        return handleGenericFlux(HttpMethod.GET, urlCity(cityName), INPECityResponse.class, MediaType.APPLICATION_XML_VALUE)
                .doOnError(throwable -> LOG.error("=== Error finding city by name ===", throwable));
    }

    protected UriComponents urlCity(String cityName) {
        return urlBuilder()
                .pathSegment(CITY_LIST)
                .queryParam("city", cityName)
                .build();
    }

    protected UriComponents urlWeather(Integer cityCode) {
        return uriBuilder(CITY_WEATHER.replaceAll("#", String.valueOf(cityCode)));
    }

    protected UriComponents urlWeatherNextWeek(Integer cityCode) {
        return uriBuilder(CITY_WEATHER.replaceAll("#", "7dias/" + cityCode));
    }

    protected UriComponents uriBuilder(String pathSegment) {
        return urlBuilder()
                .pathSegment(pathSegment)
                .build();
    }

}
