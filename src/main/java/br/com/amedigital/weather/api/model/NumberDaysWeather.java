package br.com.amedigital.weather.api.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NumberDaysWeather {

    N4(4),
    N7(7);

    NumberDaysWeather(Integer value) {
        this.value = value;
    }

    private Integer value;

    @JsonValue
    public Integer getValue() {
        return value;
    }
}
