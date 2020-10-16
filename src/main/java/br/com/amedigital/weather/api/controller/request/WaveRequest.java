package br.com.amedigital.weather.api.controller.request;

public class WaveRequest {

    private Integer localeCode;
    private Integer day;

    public Integer getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(Integer localeCode) {
        this.localeCode = localeCode;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }
}
