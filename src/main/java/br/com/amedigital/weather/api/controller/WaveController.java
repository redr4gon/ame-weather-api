package br.com.amedigital.weather.api.controller;

import br.com.amedigital.weather.api.controller.response.WavesWeatherResponse;
import br.com.amedigital.weather.api.service.WaveService;
import br.com.amedigital.weather.api.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/wave")
public class WaveController {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherController.class);

    private final WaveService weaveService;

    public WaveController(WaveService weaveService) {
        this.weaveService = weaveService;
    }

    @GetMapping("/find-waves/{cityCode}/{dayCode}")
    public Flux<WavesWeatherResponse> findWeatherWavesToCity(@PathVariable String cityCode, @PathVariable String dayCode) {
        return weaveService.findWeatherWavesToCity(Integer.parseInt(cityCode), Integer.parseInt(dayCode))
                .doOnTerminate(() -> LOG.info("=== Finish finding waves weather to city ==="));
    }
}
