package br.com.amedigital.weather.api.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WavesWeatherEntity {

    private String id;
    private String agitation;
    private BigDecimal hight;
    private String direction;
    private BigDecimal wind;
    private String directionWind;
    private Integer cityCode;
    private String cityName;
    private LocalDateTime date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "WavesWeatherEntity {" +
                "id='" + id + '\'' +
                ", agitation='" + agitation + '\'' +
                ", hight=" + hight +
                ", direction='" + direction + '\'' +
                ", wind=" + wind +
                ", directionWind='" + directionWind + '\'' +
                ", cityCode=" + cityCode +
                ", cityName='" + cityName + '\'' +
                ", date=" + date +
                '}';
    }
}
