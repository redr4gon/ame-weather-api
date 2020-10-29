package br.com.amedigital.weather.api.service;

import br.com.amedigital.weather.api.controller.request.WeatherRequest;
import br.com.amedigital.weather.api.controller.response.WeatherResponse;
import br.com.amedigital.weather.api.exception.NotFoundException;
import br.com.amedigital.weather.api.mapper.WeatherMapper;
import br.com.amedigital.weather.api.model.ErrorMessages;
import br.com.amedigital.weather.api.model.NumberDaysWeather;
import br.com.amedigital.weather.api.repository.WeatherRepository;
import br.com.amedigital.weather.api.service.partner.INPEClientService;
import br.com.amedigital.weather.api.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class WeatherService {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherService.class);

    private final INPEClientService inpeClientService;

    private final WeatherMapper mapper;

    private final WeatherRepository weatherRepository;

    public WeatherService(INPEClientService inpeClientService, WeatherRepository weatherRepository, WeatherMapper weatherMapper){
        this.inpeClientService = inpeClientService;
        this.weatherRepository = weatherRepository;
        this.mapper = weatherMapper;
    }

    public Flux<WeatherResponse> findWeatherToCity(Integer cityCode, Integer days) {

       return inpeClientService.findWeatherToCity(cityCode, days)
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .flatMap(inpeWeatherCityResponse -> inpeWeatherCityResponse.getName() == null || inpeWeatherCityResponse.getName().equals("null") ?
                        Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)) : Mono.just(inpeWeatherCityResponse))
                .flatMapMany(response ->  weatherRepository.save(mapper.INPEWeatherCityResponseToEntity(response, cityCode)))
                .doOnError(throwable -> LOG.error("=== Error finding weather to city with code: {} ===", cityCode))
                .onErrorMap(throwable -> throwable)
                .flatMap(entity -> Flux.just(mapper.entitytoResponse(entity)));
    }

    public Flux<WeatherResponse> findWeatherToCityByName(String cityName) {

          return inpeClientService.findToCityByName(Util.removeSpace(Util.removeAccent(cityName)))
                  .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                  .flatMapMany(city -> Objects.isNull(city.getCidades()) ?
                          Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)) :
                          Flux.fromStream(city.getCidades().stream()))
                  .flatMap(c -> findWeatherToCity(c.getCode(), NumberDaysWeather.N4.getValue()))
                  .doOnError(throwable -> LOG.error("=== Error finding city with cityName: {} ===", cityName))
                  .onErrorMap(throwable -> throwable);
    }

    public Flux<WeatherResponse> findAll(WeatherRequest weather) {
        return weatherRepository.findAll(weather)
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .doOnError(throwable -> LOG.error("=== Error finding all weather  ==="))
                .onErrorMap(throwable -> throwable)
                .flatMap(entity -> Flux.just(mapper.entitytoResponse(entity)));
    }

    public Mono<WeatherResponse> findOne(String id) {
        return weatherRepository.findOne(id)
                .flatMap(optional -> optional.map(Mono::just).orElseGet(Mono::empty))
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .doOnError(throwable -> LOG.error("=== Error finding one weather with id {}  ===", id))
                .onErrorMap(throwable -> throwable)
                .map(entity -> mapper.entitytoResponse(entity));
    }

    public Flux<WeatherResponse> save(WeatherRequest weather) {

        return weatherRepository.save(mapper.requestToEntity(weather))
                .doOnError(throwable -> LOG.error("=== Error saving weather ==="))
                .onErrorMap(throwable -> throwable)
                .flatMap(entity -> Flux.just(mapper.entitytoResponse(entity)));

    }

    public Flux<WeatherResponse> update(WeatherRequest weather) {

        return weatherRepository.update(mapper.requestToEntity(weather))
                .doOnError(throwable -> LOG.error("=== Error updating weather ==="))
                .onErrorMap(throwable -> throwable)
                .flatMap(entity -> Flux.just(mapper.entitytoResponse(entity)));

    }

    public Mono<Void> softDelete(String id) {
          return findOne(id)
                  .flatMap(entity -> weatherRepository.softDelete(id))
                  .doOnError(throwable -> LOG.error("=== Error deleting weather ==="))
                  .onErrorMap(throwable -> throwable);
    }


}
