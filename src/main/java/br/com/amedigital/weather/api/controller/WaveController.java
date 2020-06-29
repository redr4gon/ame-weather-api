package br.com.amedigital.weather.api.controller;

import br.com.amedigital.weather.api.controller.response.WaveResponse;
import br.com.amedigital.weather.api.service.WaveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/wave")
public class WaveController {

    private static final Logger LOG = LoggerFactory.getLogger(WaveController.class);

    private final WaveService waveService;

    public WaveController(WaveService waveService) {
        this.waveService = waveService;
    }

    @GetMapping
    public Mono<WaveResponse> findWaveByCityName(@RequestParam("cityName") String cityName) {
        return waveService.findWaveByCityName(cityName)
                .doOnTerminate(() -> LOG.info("=== Finish finding wave to city ==="));
    }
}
