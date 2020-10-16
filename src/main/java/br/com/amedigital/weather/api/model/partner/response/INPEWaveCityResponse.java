package br.com.amedigital.weather.api.model.partner.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cidade")
public class INPEWaveCityResponse {

    private String name;
    private String uf;
    private String updatedAt;
    private Period morning;
    private Period afternoon;
    private Period night;

    public static class Period {

        private String localDate;
        private String agitation;
        private double height;
        private String direction;
        private double wind;
        private String windDirection;

        @XmlElement(name = "dia")
        public String getLocalDate() {
            return localDate;
        }

        public void setLocalDate(String localDate) {
            this.localDate = localDate;
        }

        @XmlElement(name = "agitacao")
        public String getAgitation() {
            return agitation;
        }

        public void setAgitation(String agitation) {
            this.agitation = agitation;
        }

        @XmlElement(name = "altura")
        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
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
        public double getWind() {
            return wind;
        }

        public void setWind(double wind) {
            this.wind = wind;
        }

        @XmlElement(name = "vento_dir")
        public String getWindDirection() {
            return windDirection;
        }

        public void setWindDirection(String windDirection) {
            this.windDirection = windDirection;
        }
    }

    @XmlElement(name = "nome")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "uf")
    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    @XmlElement(name = "atualizacao")
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @XmlElement(name = "manha")
    public Period getMorning() {
        return morning;
    }

    public void setMorning(Period morning) {
        this.morning = morning;
    }

    @XmlElement(name = "tarde")
    public Period getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(Period afternoon) {
        this.afternoon = afternoon;
    }

    @XmlElement(name = "noite")
    public Period getNight() {
        return night;
    }

    public void setNight(Period night) {
        this.night = night;
    }
}
