package br.com.amedigital.weather.api.service.partner;

import br.com.amedigital.weather.api.config.webclient.BaseWebClient;
import br.com.amedigital.weather.api.exception.NotFoundException;
import br.com.amedigital.weather.api.exception.messages.CityErrorMessages;
import br.com.amedigital.weather.api.model.partner.response.INPECityResponse;
import br.com.amedigital.weather.api.model.partner.response.INPEWaveCityResponse;
import br.com.amedigital.weather.api.model.partner.response.INPEWeatherCityResponse;
import br.com.amedigital.weather.api.utils.StringUtils;
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

import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class INPEClientService extends BaseWebClient {

    private static final Logger LOG = getLogger(INPEClientService.class);

    public static final String CITY_WEATHER = "cidade/{cityCode}/previsao.xml";
    public static final String CITY_LIST = "listaCidades";
    public static final String CITY_WAVE = "cidade/{cityCode}/dia/{date}/ondas.xml";

    @Autowired
    public INPEClientService(final WebClient webClient, @Value("${partner.url}") final String url) {
        super(webClient, url);
    }

    @Trace(dispatcher = true)
    public Mono<INPEWeatherCityResponse> findWeatherToCity(Integer cityCode) {
        LOG.debug("==== Find weather to city ====");

        return handleGenericMono(HttpMethod.GET, urlWeather(cityCode), INPEWeatherCityResponse.class, MediaType.APPLICATION_XML_VALUE)
                .map(item -> {
                    item.setCode(cityCode);
                    return item;
                })
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
    public Mono<INPECityResponse.City> findCityByName(String cityName) {
        LOG.debug("==== Find city by name ====");

        return handleGenericFlux(HttpMethod.GET, urlCity(cityName), INPECityResponse.class, MediaType.APPLICATION_XML_VALUE)
                .flatMap(cityResponse -> {
                    List<INPECityResponse.City> cities = cityResponse.getCities()
                            .stream()
                            .filter(city -> city.getName().equalsIgnoreCase(cityName))
                            .collect(Collectors.toList());

                    if (cities.size() != 1 || StringUtils.isEmpty(cities.get(0).getName())) {
                        return Mono.error(new NotFoundException(CityErrorMessages.CITY_NOT_FOUND));
                    }

                    return Mono.just(cities.get(0));
                })
                .single()
                .doOnError(throwable -> LOG.error("=== Error finding city by name ===", throwable));
    }

    @Trace(dispatcher = true)
    public Mono<INPEWaveCityResponse> findWaveByCity(Integer cityCode) {
        return handleGenericMono(HttpMethod.GET, urlWave(cityCode, 0), INPEWaveCityResponse.class, MediaType.APPLICATION_XML_VALUE)
                .map(inpeWaveCityResponse -> {
                    inpeWaveCityResponse.setCode(cityCode);
                    return inpeWaveCityResponse;
                })
                .doOnError(throwable -> LOG.error("=== Error finding city by name ===", throwable));
    }

    protected UriComponents urlCity(String cityName) {
        return urlBuilder()
                .pathSegment(CITY_LIST)
                .queryParam("city", cityName)
                .build();
    }

    protected UriComponents urlWeather(Integer cityCode) {
        return uriBuilder(CITY_WEATHER.replace("{cityCode}", String.valueOf(cityCode)));
    }

    protected UriComponents urlWave(Integer cityCode, Integer date) {
        return uriBuilder(CITY_WAVE
                .replace("{cityCode}", String.valueOf(cityCode))
                .replace("{date}", String.valueOf(date))
        );
    }

    protected UriComponents urlWeatherNextWeek(Integer cityCode) {
        return uriBuilder(CITY_WEATHER.replace("{cityCode}", "7dias/" + cityCode));
    }

    protected UriComponents uriBuilder(String pathSegment) {
        return urlBuilder()
                .pathSegment(pathSegment)
                .build();
    }

}
