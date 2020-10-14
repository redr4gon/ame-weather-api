package br.com.amedigital.weather.api.controller.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WavesWeatherResponse {

    private String agitation;
    private BigDecimal hight;
    private String direction;
    private BigDecimal wind;
    private String directionWind;
    private String cityName;
    private LocalDateTime date;

    public String getAgitation() {
        return agitation;
    }

    public void setAgitation(String agitation) {
        this.agitation = agitation;
    }

    public BigDecimal getHight() {
        return hight;
    }

    public void setHight(BigDecimal hight) {
        this.hight = hight;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public BigDecimal getWind() {
        return wind;
    }

    public void setWind(BigDecimal wind) {
        this.wind = wind;
    }

    public String getDirectionWind() {
        return directionWind;
    }

    public void setDirectionWind(String directionWind) {
        this.directionWind = directionWind;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "WavesWeatherEntity {" +
                " agitation=" + agitation + '\'' +
                ", hight=" + hight +
                ", direction=" + direction +
                ", wind=" + wind +
                ", directionWind=" + directionWind +
                ", cityName='" + cityName + '\'' +
                ", date=" + date +
                '}';
    }
}
