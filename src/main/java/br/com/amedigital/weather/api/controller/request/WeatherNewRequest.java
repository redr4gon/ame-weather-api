package br.com.amedigital.weather.api.controller.request;

import br.com.amedigital.weather.api.model.WeatherType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class WeatherNewRequest {

    private String id;

    @NotEmpty
    private String weatherCity;

    @NotNull
    private LocalDate weatherDate;

    @NotNull
    private Integer maximumTemperature;

    @NotNull
    private Integer minimumTemperature;

    @NotNull
    private WeatherType weather;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeatherCity() {
        return weatherCity;
    }

    public void setWeatherCity(String weatherCity) {
        this.weatherCity = weatherCity;
    }

    public LocalDate getWeatherDate() {
        return weatherDate;
    }

    public void setWeatherDate(LocalDate weatherDate) {
        this.weatherDate = weatherDate;
    }

    public Integer getMaximumTemperature() {
        return maximumTemperature;
    }

    public void setMaximumTemperature(Integer maximumTemperature) {
        this.maximumTemperature = maximumTemperature;
    }

    public Integer getMinimumTemperature() {
        return minimumTemperature;
    }

    public void setMinimumTemperature(Integer minimumTemperature) {
        this.minimumTemperature = minimumTemperature;
    }

    public WeatherType getWeather() {
        return weather;
    }

    public void setWeather(WeatherType weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "WeatherNewRequest{" +
                "id='" + id + '\'' +
                ", weatherCity='" + weatherCity + '\'' +
                ", weatherDate=" + weatherDate +
                ", maximumTemperature=" + maximumTemperature +
                ", minimumTemperature=" + minimumTemperature +
                ", weather=" + weather +
                '}';
    }
}
