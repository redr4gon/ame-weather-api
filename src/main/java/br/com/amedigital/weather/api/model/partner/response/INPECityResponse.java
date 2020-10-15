package br.com.amedigital.weather.api.model.partner.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cidades")
public class INPECityResponse {

    private List<Cidade> cidades;

    public static class Cidade {
        private Integer code;
        private String name;
        private String state;

        @XmlElement(name = "id")
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
    }

    @XmlElement(name = "cidade")
    public List<Cidade> getCidades() {
        return cidades;
    }

    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
    }
}
