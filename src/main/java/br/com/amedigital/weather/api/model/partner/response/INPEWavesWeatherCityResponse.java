package br.com.amedigital.weather.api.model.partner.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.List;

@XmlRootElement(name = "cidade")
public class INPEWavesWeatherCityResponse {

    private String name;
    private String state;
    private String updatedAt;
    private List<PeriodWeather> periodWeather;

    public static class PeriodWeather {

        private String localDate;
        private String agitation;
        private BigDecimal hight;
        private String direction;
        private BigDecimal wind;
        private String directionWind;

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
        public BigDecimal getHight() {
            return hight;
        }

        public void setHight(BigDecimal hight) {
            this.hight = hight;
        }

        @XmlElement(name = "direcao")
        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        @XmlElement(name = "vento")
        public BigDecimal getWind() {
            return wind;
        }

        public void setWind(BigDecimal wind) {
            this.wind = wind;
        }

        @XmlElement(name = "vento_dir")
        public String getDirectionWind() {
            return directionWind;
        }

        public void setDirectionWind(String directionWind) {
            this.directionWind = directionWind;
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

    @XmlElements({
            @XmlElement(name = "manha"),
            @XmlElement(name = "tarde"),
            @XmlElement(name = "noite")
    })
    public List<PeriodWeather> getPeriodWeather() {
        return periodWeather;
    }

    public void setPeriodWeather(List<PeriodWeather> periodWeather) {
        this.periodWeather = periodWeather;
    }
}
