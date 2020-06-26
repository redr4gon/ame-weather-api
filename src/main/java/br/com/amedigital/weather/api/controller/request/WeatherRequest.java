package br.com.amedigital.weather.api.controller.request;

public class WeatherRequest {

    private String cityName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "WeatherRequest{" +
                "cityName='" + cityName + '\'' +
                '}';
    }
}
