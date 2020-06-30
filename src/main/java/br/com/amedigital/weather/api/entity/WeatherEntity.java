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

    public WeatherEntity(Builder builder) {
        this.id = builder.id;
        this.maximumTemperature = builder.maximumTemperature;
        this.minimumTemperature = builder.minimumTemperature;
        this.weather = builder.weather;
        this.cityCode = builder.cityCode;
        this.cityName = builder.cityName;
        this.date = builder.date;
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

        public String id;
        public Integer maximumTemperature;
        public Integer minimumTemperature;
        public WeatherType weather;
        public Integer cityCode;
        public String cityName;
        public LocalDate date;

        public Builder maximumTemperature(Integer val) {
            this.maximumTemperature = val;
            return this;
        }

        public Builder minimumTemperature(Integer val) {
            this.minimumTemperature = val;
            return this;
        }

        public Builder weather(WeatherType val) {
            this.weather = val;
            return this;
        }

        public Builder cityCode(Integer val) {
            this.cityCode = val;
            return this;
        }

        public Builder cityName(String val) {
            this.cityName = val;
            return this;
        }

        public Builder date(LocalDate val) {
            this.date = val;
            return this;
        }

        public WeatherEntity build() {
            return new WeatherEntity(this);
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
