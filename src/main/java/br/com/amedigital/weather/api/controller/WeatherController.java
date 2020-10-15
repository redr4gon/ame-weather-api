package br.com.amedigital.weather.api.controller;

import br.com.amedigital.weather.api.controller.request.WeatherRequest;
import br.com.amedigital.weather.api.controller.response.WavesWeatherResponse;
import br.com.amedigital.weather.api.controller.response.WeatherResponse;
import br.com.amedigital.weather.api.model.NumberDaysWeather;
import br.com.amedigital.weather.api.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/weather")
public class WeatherController {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherController.class);

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{cityCode}")
    public Flux<WeatherResponse> findWeatherToCity(@PathVariable String cityCode) {
        return weatherService.findWeatherToCity(Integer.parseInt(cityCode), NumberDaysWeather.N4.getValue())
                .doOnTerminate(() -> LOG.info("=== Finish finding weather to city ==="));
    }

    @GetMapping("/find-to-seven-days/{cityCode}")
    public Flux<WeatherResponse> findWeatherToCityToSevenDays(@PathVariable String cityCode) {
        return weatherService.findWeatherToCity(Integer.parseInt(cityCode), NumberDaysWeather.N7.getValue())
                .doOnTerminate(() -> LOG.info("=== Finish finding weather to city ==="));
    }

    @GetMapping("/find-by-name/{cityName}")
    public Flux<WeatherResponse> findWeatherToCityByName(@PathVariable String cityName) {
        return weatherService.findWeatherToCityByName(cityName)
                .doOnTerminate(() -> LOG.info("=== Finish finding weather to city by name ==="));
    }

    @GetMapping("/find-waves/{cityCode}/{dayCode}")
    public Flux<WavesWeatherResponse> findWeatherWavesToCity(@PathVariable String cityCode, @PathVariable String dayCode) {
         return weatherService.findWeatherWavesToCity(Integer.parseInt(cityCode), Integer.parseInt(dayCode))
                 .doOnTerminate(() -> LOG.info("=== Finish finding waves weather to city ==="));
    }

    //CRUD
    @GetMapping("/find-all")
    public Flux<WeatherResponse> findAll() {
        return weatherService.findAll()
                .doOnTerminate(() -> LOG.info("=== Finish finding all weather ==="));
    }

    @GetMapping("/find-one/{id}")
    public Mono<WeatherResponse> findOne(@PathVariable String id) {
        return weatherService.findOne(id)
                .doOnTerminate(() -> LOG.info("=== Finish finding one weather ==="));
    }

    @PostMapping
    public Flux<WeatherResponse> save(@RequestBody WeatherRequest weather) {
        return weatherService.save(weather)
                .doOnTerminate(() -> LOG.info("=== Finish save weather ==="));
    }

    @PutMapping
    public Flux<WeatherResponse> update(@RequestBody WeatherRequest weather) {
        return weatherService.update(weather)
                .doOnTerminate(() -> LOG.info("=== Finish update weather ==="));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
         return weatherService.delete(id)
                 .doOnTerminate(() -> LOG.info("=== Finish delete weather ==="));
    }
}
