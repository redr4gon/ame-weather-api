package br.com.amedigital.weather.api.controller.response;

import br.com.amedigital.weather.api.model.WeatherType;

import java.time.LocalDate;

public class WeatherResponse {

    private String weatherCity;
    private LocalDate weatherDate;
    private Integer maximumTemperature;
    private Integer minimumTemperature;
    private WeatherType weather;

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

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "weatherCity='" + weatherCity + '\'' +
                ", weatherDate=" + weatherDate +
                ", maximumTemperature=" + maximumTemperature +
                ", minimumTemperature=" + minimumTemperature +
                ", weather=" + weather +
                '}';
    }
}
