package br.com.amedigital.weather.api.controller;

import br.com.amedigital.weather.api.controller.response.WeatherResponse;
import br.com.amedigital.weather.api.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/weather")
public class WeatherController {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherController.class);

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public Flux<WeatherResponse> findWeatherToCity(@RequestParam("cityName") String cityName) {
        return weatherService.findWeatherToCity(cityName)
                .doOnTerminate(() -> LOG.info("=== Finish finding weather to city ==="));
    }

    @GetMapping("/week")
    public Flux<WeatherResponse> findWeatherToCityToNextWeek(@RequestParam("cityName") String cityName) {
        return weatherService.findWeatherToCityToNextWeek(cityName)
                .doOnTerminate(() -> LOG.info("=== Finish finding weather to city ==="));
    }
}
