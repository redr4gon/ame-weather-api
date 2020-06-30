package br.com.amedigital.weather.api.controller;

import br.com.amedigital.weather.api.controller.request.WeatherNewRequest;
import br.com.amedigital.weather.api.controller.request.WeatherUpdateRequest;
import br.com.amedigital.weather.api.controller.response.WeatherResponse;
import br.com.amedigital.weather.api.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/weather")
public class WeatherController {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherController.class);

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public Flux<WeatherResponse> findWeatherToCity(@RequestParam(value = "cityName", required = false) String cityName) {
        return weatherService.findWeatherToCity(cityName)
                .doOnTerminate(() -> LOG.info("=== Finish finding weather to city ==="));
    }

    @GetMapping("/{id}")
    public Mono<WeatherResponse> findOneWeather(@PathVariable("id") String weatherId) {
        return weatherService.findOneWeather(weatherId)
                .doOnTerminate(() -> LOG.info("=== Finish finding weather by id ==="));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<WeatherNewRequest> createWeather(@RequestBody @Valid WeatherNewRequest weather) {
        return weatherService.createWeather(weather)
                .doOnTerminate(() -> LOG.info("=== Finish creation weather ==="));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> updateWeather(@PathVariable("id") String id, @RequestBody @Valid WeatherUpdateRequest weather) {
        return weatherService.updateWeather(id, weather)
                .doOnTerminate(() -> LOG.info("=== Finish update weather ==="));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteWeather(@PathVariable("id") String id) {
        return weatherService.deleteWeather(id)
                .doOnTerminate(() -> LOG.info("=== Finish deletion weather ==="));
    }

    @GetMapping("/week")
    public Flux<WeatherResponse> findWeatherToCityToNextWeek(@RequestParam("cityName") String cityName) {
        return weatherService.findWeatherToCityToNextWeek(cityName)
                .doOnTerminate(() -> LOG.info("=== Finish finding weather to city ==="));
    }
}
