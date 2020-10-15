package br.com.amedigital.weather.api.service.partner;

import br.com.amedigital.weather.api.config.webclient.BaseWebClient;
import br.com.amedigital.weather.api.model.NumberDaysWeather;
import br.com.amedigital.weather.api.model.partner.response.INPECityResponse;
import br.com.amedigital.weather.api.model.partner.response.INPEWavesWeatherCityResponse;
import br.com.amedigital.weather.api.model.partner.response.INPEWeatherCityResponse;
import com.newrelic.api.agent.Trace;
import org.apache.commons.lang3.StringUtils;
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
    public static final String CITY_WEATHER_7_DAYS = "cidade/7dias/#/previsao.xml";
    public static final String CITY_WAVES_WEATHER = "cidade/#/dia/@/ondas.xml";
    public static final String CITY = "listaCidades";

    @Autowired
    public INPEClientService(final WebClient webClient, @Value("${partner.url}") final String url) {
        super(webClient, url);
    }

    @Trace(dispatcher = true)
    public Mono<INPEWeatherCityResponse> findWeatherToCity(Integer cityCode, Integer days) {
        LOG.debug("==== Find weather to city ====");
        String url = days.equals(NumberDaysWeather.N4.getValue()) ? CITY_WEATHER : CITY_WEATHER_7_DAYS;

        return handleGenericMono(HttpMethod.GET, urlWeather(url.replaceAll("#", String.valueOf(cityCode))), INPEWeatherCityResponse.class, MediaType.APPLICATION_XML_VALUE)
                        .doOnError(throwable -> LOG.error("=== Error finding weather to city ===", throwable));
    }

    @Trace(dispatcher = true)
    public Mono<INPECityResponse> findToCityByName(String cityName) {
        LOG.debug("==== Find city by name ====");

        return handleGenericMono(HttpMethod.GET, urlCity(String.valueOf(cityName)), INPECityResponse.class, MediaType.APPLICATION_XML_VALUE)
                .doOnError(throwable -> LOG.error("=== Error finding to city ===", throwable));
    }

    @Trace(dispatcher = true)
    public Mono<INPEWavesWeatherCityResponse> findWeatherWavesToCity(Integer cityCode, Integer day) {
        LOG.debug("==== Find weather waves to city ====");
        String url = StringUtils.replaceEach(CITY_WAVES_WEATHER, new String[]{"#", "@"}, new String[]{String.valueOf(cityCode), String.valueOf(day)});

        return handleGenericMono(HttpMethod.GET, urlWeather(url), INPEWavesWeatherCityResponse.class, MediaType.APPLICATION_XML_VALUE)
                .doOnError(throwable -> LOG.error("=== Error finding weather waves to city ===", throwable));
    }

    protected UriComponents urlWeather(String url) {
        return urlBuilder()
                .pathSegment(url)
                .build();
    }

    protected UriComponents urlCity(String cityName) {
        return urlBuilder()
                .pathSegment(CITY)
                .queryParam("city", cityName)
                .build();
    }
}
