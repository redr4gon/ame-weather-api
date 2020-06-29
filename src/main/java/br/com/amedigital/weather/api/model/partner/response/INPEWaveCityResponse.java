package br.com.amedigital.weather.api.model.partner.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cidade")
public class INPEWaveCityResponse {

    private Integer code;
    private String name;
    private String state;
    private String updatedAt;
    private Wave morning;
    private Wave afternoon;
    private Wave night;

    public static class Wave {

        private String date;
        private String agitation;
        private Double height;
        private String direction;
        private Double drift;
        private String driftDirection;

        @XmlElement(name = "dia")
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        @XmlElement(name = "agitacao")
        public String getAgitation() {
            return agitation;
        }

        public void setAgitation(String agitation) {
            this.agitation = agitation;
        }

        @XmlElement(name = "altura")
        public Double getHeight() {
            return height;
        }

        public void setHeight(Double height) {
            this.height = height;
        }

        @XmlElement(name = "direcao")
        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        @XmlElement(name = "vento")
        public Double getDrift() {
            return drift;
        }

        public void setDrift(Double drift) {
            this.drift = drift;
        }

        @XmlElement(name = "vento_dir")
        public String getDriftDirection() {
            return driftDirection;
        }

        public void setDriftDirection(String driftDirection) {
            this.driftDirection = driftDirection;
        }
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @XmlElement(name = "nome")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "uf")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @XmlElement(name = "atualizacao")
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @XmlElement(name = "manha")
    public Wave getMorning() {
        return morning;
    }

    public void setMorning(Wave morning) {
        this.morning = morning;
    }

    @XmlElement(name = "tarde")
    public Wave getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(Wave afternoon) {
        this.afternoon = afternoon;
    }

    @XmlElement(name = "noite")
    public Wave getNight() {
        return night;
    }

    public void setNight(Wave night) {
        this.night = night;
    }
}
