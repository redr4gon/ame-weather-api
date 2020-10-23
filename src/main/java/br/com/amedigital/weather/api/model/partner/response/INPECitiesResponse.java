package br.com.amedigital.weather.api.model.partner.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "cidades")
public class INPECitiesResponse {

    private List<City> cities;

    public static class City {

        private String name;
        private String state;
        private Integer cityCode;

        @XmlElement(name = "nome")
        public String getName() { return name; }

        public void setName(String name) { this.name = name; }

        @XmlElement(name = "uf")
        public String getState() { return state; }

        public void setState(String state) { this.state = state; }

        @XmlElement(name = "id")
        public Integer getCityCode() { return cityCode; }

        public void setCityCode(Integer cityCode) { this.cityCode = cityCode; }
    }

    @XmlElement(name = "cidade")
    public List<City> getCities() { return cities; }

    public void setCities(List<City> cities) { this.cities = cities; }

    public void addCities(City city) {
        if (this.cities == null) {
            this.cities = new ArrayList<>();
        }
        this.cities.add(city);
    }
}
