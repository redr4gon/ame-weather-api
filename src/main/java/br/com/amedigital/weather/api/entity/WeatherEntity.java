package br.com.amedigital.weather.api.entity;

import br.com.amedigital.weather.api.model.WeatherType;

import java.time.LocalDate;

public class WeatherEntity {

    private String id;
    private Integer maximumTemperature;
    private Integer minimumTemperature;
    private WeatherType weather;
    private Integer cityCode;
    private String cityName;
    private LocalDate date;

    private WeatherEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

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

    public static final class Builder {
        private WeatherEntity weatherEntity;

        private Builder() {
            weatherEntity = new WeatherEntity();
        }

        public static Builder aWeatherEntity() {
            return new Builder();
        }

        public Builder id(String id) {
            weatherEntity.setId(id);
            return this;
        }

        public Builder maximumTemperature(Integer maximumTemperature) {
            weatherEntity.setMaximumTemperature(maximumTemperature);
            return this;
        }

        public Builder minimumTemperature(Integer minimumTemperature) {
            weatherEntity.setMinimumTemperature(minimumTemperature);
            return this;
        }

        public Builder weather(WeatherType weather) {
            weatherEntity.setWeather(weather);
            return this;
        }

        public Builder cityCode(Integer cityCode) {
            weatherEntity.setCityCode(cityCode);
            return this;
        }

        public Builder cityName(String cityName) {
            weatherEntity.setCityName(cityName);
            return this;
        }

        public Builder date(LocalDate date) {
            weatherEntity.setDate(date);
            return this;
        }

        public WeatherEntity build() {
            return weatherEntity;
        }
    }

    @Override
    public String toString() {
        return "WeatherEntity{" +
                "id='" + id + '\'' +
                ", maximumTemperature=" + maximumTemperature +
                ", minimumTemperature=" + minimumTemperature +
                ", weather=" + weather +
                ", cityCode=" + cityCode +
                ", cityName='" + cityName + '\'' +
                ", date=" + date +
                '}';
    }
}
