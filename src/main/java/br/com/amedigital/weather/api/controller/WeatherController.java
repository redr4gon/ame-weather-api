package br.com.amedigital.weather.api.controller;

import br.com.amedigital.weather.api.controller.request.WeatherRequest;
import br.com.amedigital.weather.api.controller.response.WeatherResponse;
import br.com.amedigital.weather.api.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/weather")
public class WeatherController {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherController.class);

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public Flux<WeatherResponse> findWeatherToCity(@RequestBody WeatherRequest weatherRequest) {
        return weatherService.findWeatherToCity(weatherRequest)
                .doOnTerminate(() -> LOG.info("=== Finish finding weather to city ==="));
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE, value = "/name")
    public Flux<WeatherResponse> findWeatherToCityName(@RequestBody WeatherRequest weatherRequest) {
        return weatherService.findWeatherToCityName(weatherRequest)
                .doOnTerminate(() -> LOG.info("=== Finish finding weather to city ==="));
    }
}
