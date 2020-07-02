package br.com.amedigital.weather.api.service;

import br.com.amedigital.weather.api.controller.request.WeatherNewRequest;
import br.com.amedigital.weather.api.controller.request.WeatherRequest;
import br.com.amedigital.weather.api.controller.request.WeatherUpdateRequest;
import br.com.amedigital.weather.api.controller.response.WeatherResponse;
import br.com.amedigital.weather.api.entity.WeatherEntity;
import br.com.amedigital.weather.api.exception.BadRequestException;
import br.com.amedigital.weather.api.exception.NotFoundException;
import br.com.amedigital.weather.api.exception.messages.WeatherErrorMessages;
import br.com.amedigital.weather.api.mapper.WeatherMapper;
import br.com.amedigital.weather.api.model.ErrorMessages;
import br.com.amedigital.weather.api.model.partner.response.INPEWeatherCityResponse;
import br.com.amedigital.weather.api.repository.WeatherRepository;
import br.com.amedigital.weather.api.service.partner.INPEClientService;
import br.com.amedigital.weather.api.utils.StringUtils;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

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
        if (StringUtils.isEmpty(weatherRequest.getCityName())) {
            return weatherRepository.findAllWeather(weatherRequest);
        }

        AtomicInteger cityCode = new AtomicInteger();

        return inpeClientService
                .findCityByName(weatherRequest.getCityName(), weatherRequest.getState())
                .flatMapMany(inpeWeatherCityResponse -> {
                    cityCode.set(inpeWeatherCityResponse.getCode());
                    return weatherRepository.findByCityCode(inpeWeatherCityResponse.getCode(), 3);
                })
                .collectList()
                .flatMapMany(weatherEntities -> filterDaysByCity(weatherEntities, 4, (a) -> inpeClientService.findWeatherToCity(cityCode.get())))
                .switchIfEmpty(Mono.error(new BadRequestException(ErrorMessages.GENERIC_NOT_FOUND_EXCEPTION)))
                .flatMap(weatherEntity -> StringUtils.isEmpty(weatherEntity.getId()) ? weatherRepository.save(weatherEntity) : Mono.just(weatherEntity))
                .doOnError(throwable -> LOG.error("=== Error finding weather to city with name: {} ===", weatherRequest.getCityName()))
                .onErrorMap(throwable -> throwable)
                .map(mapper::entityToResponse);
    }

    public Flux<WeatherResponse> findWeatherToCityToNextWeek(String cityName, String state) {
        AtomicInteger cityCode = new AtomicInteger();

        return inpeClientService
                .findCityByName(cityName, state)
                .flatMapMany(inpeWeatherCityResponse -> {
                    cityCode.set(inpeWeatherCityResponse.getCode());
                    return weatherRepository.findByCityCode(inpeWeatherCityResponse.getCode(), 6);
                })
                .collectList()
                .flatMapMany(weatherEntities -> filterDaysByCity(weatherEntities, 7, (a) -> inpeClientService.findWeatherToCityToNextWeek(cityCode.get())))
                .flatMap(weatherEntity -> StringUtils.isEmpty(weatherEntity.getId()) ? weatherRepository.save(weatherEntity) : Mono.just(weatherEntity))
                .doOnError(throwable -> LOG.error("=== Error finding weather to city with name: {} ===", cityName))
                .onErrorMap(throwable -> throwable)
                .map(mapper::entityToResponse);
    }


    private Publisher<? extends WeatherEntity> filterDaysByCity(List<WeatherEntity> weatherEntities, int days, Function<Void, Mono<INPEWeatherCityResponse>> function) {
        if (weatherEntities.size() == days) {
            return Flux.fromIterable(weatherEntities);
        }

        return function.apply(null)
                .single()
                .map(mapper::INPEWeatherCityResponseToEntity)
                .flatMapMany(entityList -> Flux.fromStream(entityList.stream()))
                .mergeWith(Flux.fromIterable(weatherEntities))
                .filter(weatherEntity -> {
                    if (StringUtils.isNotEmpty(weatherEntity.getId())) {
                        return true;
                    }

                    if (weatherEntity.getDate().isAfter(LocalDate.now().plusDays(days - 1))) {
                        return false;
                    }

                    return weatherEntities.stream().noneMatch(w -> w.getDate().isEqual(weatherEntity.getDate()));
                });
    }

    public Mono<WeatherResponse> findOneWeather(String weatherId) {
        return weatherRepository.findById(weatherId)
                .switchIfEmpty(Mono.error(new NotFoundException(WeatherErrorMessages.WEATHER_NOT_FOUND)));
    }

    public Mono<WeatherNewRequest> createWeather(WeatherNewRequest weather) {
        return inpeClientService
                .findCityByName(weather.getWeatherCity(), null)
                .flatMap(inpeWeatherCityResponse -> {
                    WeatherEntity weatherEntity = WeatherEntity.Builder
                            .aWeatherEntity()
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
