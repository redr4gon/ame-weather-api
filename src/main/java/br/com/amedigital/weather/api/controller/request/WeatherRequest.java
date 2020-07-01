package br.com.amedigital.weather.api.controller.request;

import java.time.LocalDate;

public class WeatherRequest {

    private String cityName;
    private LocalDate date;
    private Integer minimumTemperature;
    private Integer maximumTemperature;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getMinimumTemperature() {
        return minimumTemperature;
    }

    public void setMinimumTemperature(Integer minimumTemperature) {
        this.minimumTemperature = minimumTemperature;
    }

    public Integer getMaximumTemperature() {
        return maximumTemperature;
    }

    public void setMaximumTemperature(Integer maximumTemperature) {
        this.maximumTemperature = maximumTemperature;
    }

    @Override
    public String toString() {
        return "WeatherRequest{" +
                "cityName='" + cityName + '\'' +
                ", date=" + date +
                ", minimumTemperature=" + minimumTemperature +
                ", maximumTemperature=" + maximumTemperature +
                '}';
    }
    
}
