package br.com.amedigital.weather.api.entity;

import br.com.amedigital.weather.api.model.WaveAgitation;

import java.time.LocalDate;

public class WaveEntity {

    private String id;
    private Integer cityCode;
    private String cityName;
    private LocalDate updatedAt;
    private String date;
    private WaveAgitation agitation;
    private Double height;
    private String direction;
    private Double drift;
    private String driftDirection;
    private char period;

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

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public WaveAgitation getAgitation() {
        return agitation;
    }

    public void setAgitation(WaveAgitation agitation) {
        this.agitation = agitation;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Double getDrift() {
        return drift;
    }

    public void setDrift(Double drift) {
        this.drift = drift;
    }

    public String getDriftDirection() {
        return driftDirection;
    }

    public void setDriftDirection(String driftDirection) {
        this.driftDirection = driftDirection;
    }

    public char getPeriod() {
        return period;
    }

    public void setPeriod(char period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "WaveEntity{" +
                "id='" + id + '\'' +
                ", cityCode=" + cityCode +
                ", cityName='" + cityName + '\'' +
                ", updatedAt=" + updatedAt +
                ", date=" + date +
                ", agitation=" + agitation +
                ", height=" + height +
                ", direction='" + direction + '\'' +
                ", drift=" + drift +
                ", driftDirection='" + driftDirection + '\'' +
                ", period=" + period +
                '}';
    }
}
