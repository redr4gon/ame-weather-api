package br.com.amedigital.weather.api.mapper;

import br.com.amedigital.weather.api.controller.response.WeatherResponse;
import br.com.amedigital.weather.api.entity.WeatherEntity;
import br.com.amedigital.weather.api.model.WeatherType;
import br.com.amedigital.weather.api.model.partner.response.INPEWeatherCityResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class WeatherMapper {

    public WeatherResponse entitytoResponse(WeatherEntity weatherEntity) {

        WeatherResponse weather = new WeatherResponse();
        weather.setWeather(weatherEntity.getWeather());
        weather.setMaximumTemperature(weatherEntity.getMaximumTemperature());
        weather.setMinimumTemperature(weatherEntity.getMinimumTemperature());
        weather.setWeatherCity(weatherEntity.getCityName());
        weather.setWeatherDate(weatherEntity.getDate());

        return weather;
    }

    public List<WeatherEntity> INPEWeatherCityResponseToEntity(INPEWeatherCityResponse inpeWeatherCityResponse, Integer code) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return inpeWeatherCityResponse.getWeather().stream()
                .flatMap(w ->  {
                    WeatherEntity entity = new WeatherEntity();
                    entity.setCityCode(code);
                    entity.setCityName(inpeWeatherCityResponse.getName());
                    entity.setMaximumTemperature(w.getMaxTemperature());
                    entity.setMinimumTemperature(w.getMinTemperature());
                    entity.setWeather(WeatherType.valueOf(w.getWeather()));
                    entity.setDate(LocalDate.parse(w.getLocalDate(), formatter));
                    return Stream.of(entity);
                }).collect(Collectors.toList());
    }
}
