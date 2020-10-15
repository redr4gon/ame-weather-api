package br.com.amedigital.weather.api.service.partner;

import br.com.amedigital.weather.api.config.webclient.BaseWebClient;
import br.com.amedigital.weather.api.controller.request.WaveRequest;
import br.com.amedigital.weather.api.model.partner.response.INPECitiesResponse;
import br.com.amedigital.weather.api.model.partner.response.INPEWaveCityResponse;
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
import reactor.core.publisher.Mono;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class INPEClientService extends BaseWebClient {


    private static final Logger LOG = getLogger(INPEClientService.class);

    public static final String CITY_WEATHER = "cidade/#/previsao.xml";
    public static final String CITY_WEATHER_SEVEN_DAYS = "cidade/7dias/#/previsao.xml";
    public static final String CITY = "listaCidades";
    public static final String CITY_WAVE_LOCALE = "cidade/#/";
    public static final String CITY_WAVE_DAY = "dia/#/ondas.xml";

    @Autowired
    public INPEClientService(final WebClient webClient, @Value("${partner.url}") final String url) {
        super(webClient, url);
    }

    @Trace(dispatcher = true)
    public Mono<INPEWeatherCityResponse> findWeatherToCity(Integer cityCode, Integer qtdeDias) {
        LOG.debug("==== Find weather to city ====");

        return handleGenericMono(HttpMethod.GET, urlWeather(cityCode, qtdeDias), INPEWeatherCityResponse.class, MediaType.APPLICATION_XML_VALUE)
                .doOnError(throwable -> LOG.error("=== Error finding weather to city ===", throwable));
    }

    @Trace(dispatcher = true)
    public Mono<INPECitiesResponse> findCityToDescription(String cityDescription) {
        LOG.debug("==== Find city ====");

        return handleGenericMono(HttpMethod.GET, urlCity(cityDescription), INPECitiesResponse.class, MediaType.APPLICATION_XML_VALUE)
                .doOnError(throwable -> LOG.error("=== Error finding city ===", throwable));
    }

    @Trace(dispatcher = true)
    public Mono<INPEWaveCityResponse> findWave(WaveRequest waveRequest) {
        LOG.debug("==== Find city ====");

        return handleGenericMono(HttpMethod.GET, urlWave(waveRequest), INPEWaveCityResponse.class, MediaType.APPLICATION_XML_VALUE)
                .doOnError(throwable -> LOG.error("=== Error finding wave city ===", throwable));
    }

    protected UriComponents urlWeather(Integer cityCode, Integer qtdeDias) {
        return urlBuilder()
                .pathSegment((qtdeDias.equals(4) ? CITY_WEATHER : CITY_WEATHER_SEVEN_DAYS).replaceAll("#", String.valueOf(cityCode)))
                .build();
    }

    protected UriComponents urlCity(String cityDescription) {
        return urlBuilder()
                .pathSegment(CITY)
                .queryParam("city", cityDescription)
                .build();
    }
    protected UriComponents urlWave(WaveRequest waveRequest) {
        return urlBuilder()
                .pathSegment(CITY_WAVE_LOCALE.replaceAll("#", String.valueOf(waveRequest.getLocaleCode())) + "" +
                             CITY_WAVE_DAY.replaceAll("#", String.valueOf(waveRequest.getDay())))
                .build();
    }

}
