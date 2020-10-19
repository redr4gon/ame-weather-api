package br.com.amedigital.weather.api.controller;

import br.com.amedigital.weather.api.controller.request.WeatherRequest;
import br.com.amedigital.weather.api.controller.response.WeatherResponse;
import br.com.amedigital.weather.api.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @GetMapping(value = "/lista")
    public Flux<WeatherResponse> findAllWeather() {
        return weatherService.findAllWeather()
                .doOnTerminate(() -> LOG.info("=== Finish finding all weather to city ==="));
    }

    @PostMapping(value = "/one")
    public Mono<WeatherResponse> findOneWeather(@RequestParam String id) {
        return weatherService.findOneWeather(id)
                .doOnTerminate(() -> LOG.info("=== Finish finding one weather to city ==="));
    }

    @PostMapping(value = "/insert")
    public Mono<WeatherResponse> insertWeather(@RequestBody WeatherRequest weatherRequest) {
        return weatherService.insertWeather(weatherRequest)
                .doOnTerminate(() -> LOG.info("=== Finished entering a climate for the city ==="));
    }

    @PutMapping(value = "/update")
    public Mono<WeatherResponse> updateWeather(@RequestBody WeatherRequest weatherRequest) {
        return weatherService.updateWeather(weatherRequest)
                .doOnTerminate(() -> LOG.info("=== Finished changing a mood for the city ==="));
    }

    @DeleteMapping(value = "/delete/{id}")
    public Mono<Integer> deleteWeather(@PathVariable String id) {
        return weatherService.deleteWeather(id)
                .doOnTerminate(() -> LOG.info("=== Finished deleting a mood for the city ==="));
    }
}
