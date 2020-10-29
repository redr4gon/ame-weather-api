package br.com.amedigital.weather.api.service;

import br.com.amedigital.weather.api.controller.response.WavesWeatherResponse;
import br.com.amedigital.weather.api.exception.NotFoundException;
import br.com.amedigital.weather.api.mapper.WaveMapper;
import br.com.amedigital.weather.api.model.ErrorMessages;
import br.com.amedigital.weather.api.repository.WaveRepository;
import br.com.amedigital.weather.api.service.partner.INPEClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WaveService {

    private static final Logger LOG = LoggerFactory.getLogger(WaveService.class);

    private final INPEClientService inpeClientService;

    private final WaveMapper waveMapper;

    private final WaveRepository waveRepository;

    public WaveService(INPEClientService inpeClientService, WaveMapper waveMapper, WaveRepository waveRepository) {
        this.inpeClientService = inpeClientService;
        this.waveMapper = waveMapper;
        this.waveRepository = waveRepository;
    }

    public Flux<WavesWeatherResponse> findWeatherWavesToCity(Integer cityCode, Integer day) {

        return inpeClientService.findWeatherWavesToCity(cityCode, day)
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .flatMap(inpeWeatherCityResponse -> inpeWeatherCityResponse.getName().equals("undefined") ?
                        Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)) : Mono.just(inpeWeatherCityResponse))
                .flatMapMany(response -> waveRepository.save(waveMapper.INPEWavesWeatherCityResponseToEntity(response, cityCode)))
                .doOnError(throwable -> LOG.error("=== Error finding weather to city with code: {} ===", cityCode))
                .onErrorMap(throwable -> throwable)
                .flatMap(entity -> Flux.just(waveMapper.entityToResponse(entity)));
    }
}
