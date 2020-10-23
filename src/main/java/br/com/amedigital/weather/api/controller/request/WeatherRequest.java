package br.com.amedigital.weather.api.controller.request;

public class WeatherRequest {

    private String id;
    private String qtDays;
    private String maximumTemperature;
    private String minimumTemperature;
    private String weather;
    private String cityCode;
    private String cityName;
    private String date;

    public WeatherRequest() {
    }

    public WeatherRequest(String id, String qtDays, String maximumTemperature, String minimumTemperature, String weather, String cityCode, String cityName, String date) {
        this.id = id;
        this.qtDays = qtDays;
        this.maximumTemperature = maximumTemperature;
        this.minimumTemperature = minimumTemperature;
        this.weather = weather;
        this.cityCode = cityCode;
        this.cityName = cityName;
        this.date = date;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getQtDays() {
        return qtDays;
    }

    public void setQtDays(String qtDays) {
        this.qtDays = qtDays;
    }

    public String getMaximumTemperature() { return maximumTemperature; }

    public void setMaximumTemperature(String maximumTemperature) { this.maximumTemperature = maximumTemperature; }

    public String getMinimumTemperature() { return minimumTemperature; }

    public void setMinimumTemperature(String minimumTemperature) { this.minimumTemperature = minimumTemperature; }

    public String getWeather() { return weather; }

    public void setWeather(String weather) { this.weather = weather; }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }
}
