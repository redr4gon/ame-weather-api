package br.com.amedigital.weather.api.controller;

import br.com.amedigital.weather.api.controller.request.WaveRequest;
import br.com.amedigital.weather.api.controller.response.WaveResponse;
import br.com.amedigital.weather.api.service.WaveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/wave")
public class WaveController {

    private static final Logger LOG = LoggerFactory.getLogger(WaveController.class);

    private final WaveService waveService;

    public WaveController(WaveService waveService) {
        this.waveService = waveService;
    }

    @PostMapping
    public Mono<WaveResponse> findWave(@RequestBody WaveRequest waveRequest) {
        return waveService.findWave(waveRequest)
                .doOnTerminate(() -> LOG.info("=== Finish finding city to description ==="));
    }
}
