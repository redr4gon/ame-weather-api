package br.com.amedigital.weather.api.controller.response;

import br.com.amedigital.weather.api.model.WeatherType;

import java.time.LocalDate;

public class WeatherResponse {

    private String id;
    private Integer weatherCityCode;
    private String weatherCity;
    private LocalDate weatherDate;
    private Integer maximumTemperature;
    private Integer minimumTemperature;
    private WeatherType weather;

    private WeatherResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getWeatherCityCode() {
        return weatherCityCode;
    }

    public void setWeatherCityCode(Integer weatherCityCode) {
        this.weatherCityCode = weatherCityCode;
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

    public static final class Builder {
        private WeatherResponse weatherResponse;

        private Builder() {
            weatherResponse = new WeatherResponse();
        }

        public static Builder aWeatherResponse() {
            return new Builder();
        }

        public Builder id(String id) {
            weatherResponse.setId(id);
            return this;
        }

        public Builder weatherCityCode(Integer weatherCityCode) {
            weatherResponse.setWeatherCityCode(weatherCityCode);
            return this;
        }

        public Builder weatherCity(String weatherCity) {
            weatherResponse.setWeatherCity(weatherCity);
            return this;
        }

        public Builder weatherDate(LocalDate weatherDate) {
            weatherResponse.setWeatherDate(weatherDate);
            return this;
        }

        public Builder maximumTemperature(Integer maximumTemperature) {
            weatherResponse.setMaximumTemperature(maximumTemperature);
            return this;
        }

        public Builder minimumTemperature(Integer minimumTemperature) {
            weatherResponse.setMinimumTemperature(minimumTemperature);
            return this;
        }

        public Builder weather(WeatherType weather) {
            weatherResponse.setWeather(weather);
            return this;
        }

        public WeatherResponse build() {
            return weatherResponse;
        }
    }
}
