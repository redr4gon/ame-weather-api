package br.com.amedigital.weather.api.mapper;

import br.com.amedigital.weather.api.controller.response.WeatherResponse;
import br.com.amedigital.weather.api.entity.WeatherEntity;
import br.com.amedigital.weather.api.model.WeatherType;
import br.com.amedigital.weather.api.model.partner.response.INPEWeatherCityResponse;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class WeatherMapper implements RowMapper<WeatherResponse> {

    public WeatherResponse entityToResponse(WeatherEntity weatherEntity) {
        return WeatherResponse.Builder
                .aWeatherResponse()
                .weather(weatherEntity.getWeather())
                .maximumTemperature(weatherEntity.getMaximumTemperature())
                .minimumTemperature(weatherEntity.getMinimumTemperature())
                .weatherCity(weatherEntity.getCityName())
                .weatherCityCode(weatherEntity.getCityCode())
                .weatherDate(weatherEntity.getDate())
                .build();
    }

    public WeatherEntity responseToEntity(WeatherResponse weatherResponse) {
        return new WeatherEntity.Builder()
                .cityCode(weatherResponse.getWeatherCityCode())
                .cityName(weatherResponse.getWeatherCity())
                .maximumTemperature(weatherResponse.getMaximumTemperature())
                .minimumTemperature(weatherResponse.getMinimumTemperature())
                .weather(weatherResponse.getWeather())
                .date(weatherResponse.getWeatherDate())
                .build();
    }

    public List<WeatherEntity> INPEWeatherCityResponseToEntity(INPEWeatherCityResponse inpeWeatherCityResponse) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return inpeWeatherCityResponse.getWeather().stream()
                .flatMap(w -> {
                    WeatherEntity entity = new WeatherEntity.Builder()
                            .cityCode(inpeWeatherCityResponse.getCode())
                            .cityName(inpeWeatherCityResponse.getName())
                            .maximumTemperature(w.getMaxTemperature())
                            .minimumTemperature(w.getMinTemperature())
                            .weather(WeatherType.valueOf(w.getWeather()))
                            .date(LocalDate.parse(w.getLocalDate(), formatter))
                            .build();

                    return Stream.of(entity);
                }).collect(Collectors.toList());
    }

    @Override
    public WeatherResponse map(ResultSet rs, StatementContext ctx) throws SQLException {
        return WeatherResponse.Builder
                .aWeatherResponse()
                .weather(WeatherType.valueOf(rs.getString("weather")))
                .maximumTemperature(rs.getInt("maximumTemperature"))
                .minimumTemperature(rs.getInt("minimumTemperature"))
                .weatherCity(rs.getString("cityName"))
                .weatherCityCode(rs.getInt("cityCode"))
                .weatherDate(rs.getDate("date").toLocalDate())
                .build();
    }

}
