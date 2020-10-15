package br.com.amedigital.weather.api.controller.request;

public class WeatherRequest {

    private String qtDays;
    private String cityCode;
    private String nameCity;

    public String getQtDays() {
        return qtDays;
    }

    public void setQtDays(String qtDays) {
        this.qtDays = qtDays;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getNameCity() {
        return nameCity;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }
}
