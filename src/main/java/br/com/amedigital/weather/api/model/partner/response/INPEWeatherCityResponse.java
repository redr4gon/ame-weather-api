package br.com.amedigital.weather.api.model.partner.response;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "cidade")
public class INPEWeatherCityResponse {

    private String name;
    private String state;
    private String updatedAt;
    private List<Weather> weather;

    public static class Weather {

        private String localDate;
        private Integer maxTemperature;
        private Integer minTemperature;
        private String weather;

        @XmlElement(name = "dia")
        public String getLocalDate() {
            return localDate;
        }

        public void setLocalDate(String localDate) {
            this.localDate = localDate;
        }

        @XmlElement(name = "maxima")
        public Integer getMaxTemperature() {
            return maxTemperature;
        }

        public void setMaxTemperature(Integer maxTemperature) {
            this.maxTemperature = maxTemperature;
        }

        @XmlElement(name = "minima")
        public Integer getMinTemperature() {
            return minTemperature;
        }

        public void setMinTemperature(Integer minTemperature) {
            this.minTemperature = minTemperature;
        }

        @XmlElement(name = "tempo")
        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
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

    @XmlElement(name = "previsao")
    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }
}
