package br.com.amedigital.weather.api.controller.request;

import br.com.amedigital.weather.api.model.WeatherType;

import javax.validation.constraints.NotNull;

public class WeatherUpdateRequest {

    @NotNull
    private Integer maximumTemperature;

    @NotNull
    private Integer minimumTemperature;

    @NotNull
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

    @Override
    public String toString() {
        return "WeatherUpdateRequest{" +
                "maximumTemperature=" + maximumTemperature +
                ", minimumTemperature=" + minimumTemperature +
                ", weather=" + weather +
                '}';
    }
}
