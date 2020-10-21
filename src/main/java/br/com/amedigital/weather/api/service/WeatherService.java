package br.com.amedigital.weather.api.service;

import br.com.amedigital.weather.api.controller.request.WeatherRequest;
import br.com.amedigital.weather.api.controller.response.WeatherResponse;
import br.com.amedigital.weather.api.entity.WeatherEntity;
import br.com.amedigital.weather.api.exception.NotFoundException;
import br.com.amedigital.weather.api.mapper.WeatherMapper;
import br.com.amedigital.weather.api.model.ErrorMessages;
import br.com.amedigital.weather.api.repository.WeatherRepository;
import br.com.amedigital.weather.api.service.partner.INPEClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class WeatherService {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherService.class);

    private final INPEClientService inpeClientService;

    private final WeatherMapper mapper;

    private final WeatherRepository weatherRepository;

    public WeatherService(INPEClientService inpeClientService, WeatherRepository weatherRepository, WeatherMapper weatherMapper) {
        this.inpeClientService = inpeClientService;
        this.weatherRepository = weatherRepository;
        this.mapper = weatherMapper;
    }

    public Flux<WeatherResponse> findWeatherToCity(WeatherRequest weatherRequest) {
        return weatherRepository.findAllCityWeather(weatherRequest)
                .collectList()
                .flatMapMany(weatherEntities -> {
                    if (weatherEntities.size() == 0) {
                        return findWeatherToCityEntity(weatherRequest);
                    } else if (weatherEntities.size() < Integer.valueOf(weatherRequest.getQtDays())) {
                        return findWeatherToCityEntityIntermediate(weatherEntities, weatherRequest.getQtDays());
                    } else {
                        return weatherRepository.findAllCityWeather(weatherRequest);
                    }
                } )
                .doOnError(throwable -> LOG.error("=== Error finding weather to city with code: {} ===", weatherRequest.getCityCode()))
                .onErrorMap(throwable -> throwable)
                .flatMap(entity -> Flux.just(mapper.entitytoResponse((WeatherEntity) entity)));
    }

    public Flux<WeatherEntity> findWeatherToCityEntity(WeatherRequest weatherRequest) {
        return inpeClientService.findWeatherToCity(Integer.valueOf(weatherRequest.getCityCode()), Integer.valueOf(weatherRequest.getQtDays()))
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .flatMap(inpeWeatherCityResponse -> inpeWeatherCityResponse.getName() == null || inpeWeatherCityResponse.getName().equals("null") ?
                        Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)) : Mono.just(inpeWeatherCityResponse))
                .flatMapMany(response ->  weatherRepository.save(mapper.INPEWeatherCityResponseToEntity(response, Integer.valueOf(weatherRequest.getCityCode())), null))
                .doOnError(throwable -> LOG.error("=== Error finding weather to city with code: {} ===", weatherRequest.getCityCode()))
                .onErrorMap(throwable -> throwable)
                .flatMap(entity -> Flux.just(entity));
    }

    public Flux<WeatherEntity> findWeatherToCityEntityIntermediate(List<WeatherEntity> weatherEntity, String qtDays) {
        return inpeClientService.findWeatherToCity(Integer.valueOf(weatherEntity.get(0).getCityCode()), Integer.valueOf(qtDays))
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .flatMap(inpeWeatherCityResponse -> inpeWeatherCityResponse.getName() == null || inpeWeatherCityResponse.getName().equals("null") ?
                        Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)) : Mono.just(inpeWeatherCityResponse))
                .flatMapMany(response ->  {
                    List<WeatherEntity> weatherEntitiesINPE = mapper.INPEWeatherCityResponseToEntity(response, Integer.valueOf(weatherEntity.get(0).getCityCode()));

                    return weatherRepository.save(weatherEntitiesINPE, weatherEntity);
                })
                .doOnError(throwable -> LOG.error("=== Error finding weather to city with code: {} ===", weatherEntity.get(0).getCityCode()))
                .onErrorMap(throwable -> throwable)
                .flatMap(entity -> Flux.just(entity));
    }

    public Flux<WeatherResponse> findWeatherToCityName(WeatherRequest weatherRequest) {
        return inpeClientService.findCityToName(weatherRequest.getCityName())
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .flatMap(inpeCitiesResponse -> inpeCitiesResponse.getCities() == null || inpeCitiesResponse.getCities().isEmpty() ?
                        Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)) : Mono.just(inpeCitiesResponse))
                .flatMap(inpeCitiesResponse -> Mono.just(inpeCitiesResponse.getCities()))
                .flatMap(cityResponses -> Mono.justOrEmpty(cityResponses.stream().findFirst()))
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .flatMapMany(cityResponse -> {
                    weatherRequest.setCityCode(cityResponse.getCityCode().toString());
                    return findWeatherToCity(weatherRequest);
                });
    }

    public Flux<WeatherResponse> findAllWeather(WeatherRequest weatherRequest) {
        return weatherRepository.findAllWeather(weatherRequest)
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .flatMap(entity -> Flux.just(mapper.entitytoResponse(entity)));
    }

    public Mono<WeatherResponse> findOneWeather(String id) {
        return weatherRepository.findOneWeather(id)
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .flatMap(entity -> Mono.just(mapper.entitytoResponse(entity)));
    }

    public Mono<WeatherResponse> insertWeather(WeatherRequest weatherRequest) {
        return weatherRepository.insertWeather(weatherRequest)
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .flatMap(entity -> Mono.just(mapper.entitytoResponse(entity)));
    }

    public Mono<WeatherResponse> updateWeather(WeatherRequest weatherRequest) {
        return weatherRepository.updateWeather(weatherRequest)
                .switchIfEmpty(Mono.empty())
                .flatMap(entity -> Mono.just(mapper.entitytoResponse(entity)));
    }

    public Mono<Integer> deleteWeather(String id) {
        return weatherRepository.deleteWeather(id)
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .flatMap(entity -> Mono.just(entity));
    }
}
