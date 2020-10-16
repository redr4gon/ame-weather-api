package br.com.amedigital.weather.api.service;

import br.com.amedigital.weather.api.controller.request.WaveRequest;
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

    private final INPEClientService inpeClientService;

    private final WaveMapper mapper;

    private final WaveRepository waveRepository;

    public WaveService(INPEClientService inpeClientService, WaveRepository waveRepository, WaveMapper waveMapper) {
        this.inpeClientService = inpeClientService;
        this.waveRepository = waveRepository;
        this.mapper = waveMapper;
    }

    public Mono<WaveResponse> findWave(WaveRequest waveRequest) {
        return inpeClientService.findWave(waveRequest)
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .flatMap(inpeCityResponse -> inpeCityResponse.getName() == null || inpeCityResponse.getName().equals("undefined") ?
                        Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)) : Mono.just(inpeCityResponse))
                .flatMap(response ->  waveRepository.save(mapper.INPEWaveResponseToEntity(response, waveRequest.getLocaleCode())))
                .doOnError(throwable -> LOG.error("=== Error finding city wave: {} ==="))
                .onErrorMap(throwable -> throwable)
                .flatMap(entity -> Mono.just(mapper.entitytoResponse(entity)));
    }
}
