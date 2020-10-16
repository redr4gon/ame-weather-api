package br.com.amedigital.weather.api.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WaveEntity {

    private String id;
    private String cityName;
    private String uf;
    private Integer cityCode;
    private LocalDate date;
    private List<Period> periods;

    public static class Period {

        private String id;
        private String period;
        private String agitation;
        private double height;
        private String direction;
        private double wind;
        private String windDirection;
        private String idWave;

        public String getId() { return id; }

        public void setId(String id) { this.id = id; }

        public String getPeriod() { return period; }

        public void setPeriod(String period) { this.period = period; }

        public String getAgitation() {
            return agitation;
        }

        public void setAgitation(String agitation) {
            this.agitation = agitation;
        }

        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public double getWind() { return wind; }

        public void setWind(double wind) {
            this.wind = wind;
        }

        public String getWindDirection() {
            return windDirection;
        }

        public void setWindDirection(String windDirection) {
            this.windDirection = windDirection;
        }

        public String getIdWave() { return idWave; }

        public void setIdWave(String idWave) { this.idWave = idWave; }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) { this.cityCode = cityCode; }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public void addPeriods(Period period) {
        if (this.periods == null) {
            this.periods = new ArrayList<>();
        }

        this.periods.add(period);
    }
}
