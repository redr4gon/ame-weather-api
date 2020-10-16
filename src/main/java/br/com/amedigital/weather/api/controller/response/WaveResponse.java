package br.com.amedigital.weather.api.controller.response;

import br.com.amedigital.weather.api.entity.WaveEntity;

import java.time.LocalDate;
import java.util.List;

public class WaveResponse {

    private String    id;
    private String    name;
    private String    uf;
    private LocalDate date;
    private List<WaveEntity.Period> periods;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) { this.date = date; }

    public List<WaveEntity.Period> getPeriods() { return periods; }

    public void setPeriods(List<WaveEntity.Period> periods) { this.periods = periods; }
}
