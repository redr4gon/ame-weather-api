package br.com.amedigital.weather.api.service;

import br.com.amedigital.weather.api.controller.response.WaveResponse;
import br.com.amedigital.weather.api.exception.NotFoundException;
import br.com.amedigital.weather.api.mapper.WaveMapper;
import br.com.amedigital.weather.api.model.ErrorMessages;
import br.com.amedigital.weather.api.repository.WaveRepository;
import br.com.amedigital.weather.api.service.partner.INPEClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WaveService {

    private static final Logger LOG = LoggerFactory.getLogger(WaveService.class);

    private final WaveMapper mapper;
    private final WaveRepository weatherRepository;
    private final INPEClientService inpeClientService;

    public WaveService(WaveMapper mapper, WaveRepository weatherRepository, INPEClientService inpeClientService) {
        this.mapper = mapper;
        this.weatherRepository = weatherRepository;
        this.inpeClientService = inpeClientService;
    }

    public Mono<WaveResponse> findWaveByCityName(String cityName) {
        return inpeClientService
                .findCityByName(cityName)
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .flatMap(city -> inpeClientService.findWaveByCity(city.getCode()))
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .flatMap(response -> weatherRepository.save(mapper.INPEWaveCityResponseToEntity(response)).then(Mono.just(response)))
                .doOnError(throwable -> LOG.error("=== Error finding weather to city with name: {} ===", cityName))
                .onErrorMap(throwable -> throwable)
                .map(mapper::entityToResponse);
    }

}
