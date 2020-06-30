package br.com.amedigital.weather.api.service;

import br.com.amedigital.weather.api.controller.request.WeatherNewRequest;
import br.com.amedigital.weather.api.controller.request.WeatherUpdateRequest;
import br.com.amedigital.weather.api.controller.response.WeatherResponse;
import br.com.amedigital.weather.api.entity.WeatherEntity;
import br.com.amedigital.weather.api.exception.NotFoundException;
import br.com.amedigital.weather.api.exception.messages.WeatherErrorMessages;
import br.com.amedigital.weather.api.mapper.WeatherMapper;
import br.com.amedigital.weather.api.model.ErrorMessages;
import br.com.amedigital.weather.api.repository.WeatherRepository;
import br.com.amedigital.weather.api.service.partner.INPEClientService;
import br.com.amedigital.weather.api.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Flux<WeatherResponse> findWeatherToCity(String cityName) {
        if (StringUtils.isEmpty(cityName)) {
            return weatherRepository.findAllWeather();
        }

        return inpeClientService
                .findCityByName(cityName)
                .flatMap(inpeWeatherCityResponse -> inpeClientService.findWeatherToCity(inpeWeatherCityResponse.getCode()))
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .flatMapMany(response -> weatherRepository.save(mapper.INPEWeatherCityResponseToEntity(response)))
                .doOnError(throwable -> LOG.error("=== Error finding weather to city with name: {} ===", cityName))
                .onErrorMap(throwable -> throwable)
                .flatMap(entity -> Flux.just(mapper.entityToResponse(entity)));
    }

    public Flux<WeatherResponse> findWeatherToCityToNextWeek(String cityName) {
        return inpeClientService
                .findCityByName(cityName)
                .flatMap(inpeWeatherCityResponse -> inpeClientService.findWeatherToCityToNextWeek(inpeWeatherCityResponse.getCode()))
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .flatMapMany(response -> weatherRepository.save(mapper.INPEWeatherCityResponseToEntity(response)))
                .doOnError(throwable -> LOG.error("=== Error finding weather to city with name: {} ===", cityName))
                .onErrorMap(throwable -> throwable)
                .flatMap(entity -> Flux.just(mapper.entityToResponse(entity)));
    }

    public Mono<WeatherResponse> findOneWeather(String weatherId) {
        return weatherRepository.findById(weatherId)
                .switchIfEmpty(Mono.error(new NotFoundException(WeatherErrorMessages.WEATHER_NOT_FOUND)));
    }

    public Mono<WeatherNewRequest> createWeather(WeatherNewRequest weather) {
        return inpeClientService
                .findCityByName(weather.getWeatherCity())
                .flatMap(inpeWeatherCityResponse -> {
                    WeatherEntity weatherEntity = new WeatherEntity.Builder()
                            .cityCode(inpeWeatherCityResponse.getCode())
                            .cityName(weather.getWeatherCity())
                            .date(weather.getWeatherDate())
                            .maximumTemperature(weather.getMaximumTemperature())
                            .minimumTemperature(weather.getMinimumTemperature())
                            .weather(weather.getWeather())
                            .build();

                    return weatherRepository.save(weatherEntity);
                })
                .map(entity -> {
                    weather.setId(entity.getId());
                    return weather;
                })
                .doOnError(throwable -> LOG.error("=== Error saving weather: {} ===", weather));
    }

    public Mono<Void> updateWeather(String id, WeatherUpdateRequest weather) {
        return weatherRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(WeatherErrorMessages.WEATHER_NOT_FOUND)))
                .flatMap(weatherResponse -> {
                    WeatherEntity weatherEntity = mapper.responseToEntity(weatherResponse);
                    weatherEntity.setId(id);
                    weatherEntity.setWeather(weather.getWeather());
                    weatherEntity.setMinimumTemperature(weather.getMinimumTemperature());
                    weatherEntity.setMaximumTemperature(weather.getMaximumTemperature());

                    return weatherRepository.update(weatherEntity);
                });
    }

    public Mono<Void> deleteWeather(String id) {
        return weatherRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(WeatherErrorMessages.WEATHER_NOT_FOUND)))
                .flatMap(weatherResponse -> weatherRepository.delete(id));
    }

}
