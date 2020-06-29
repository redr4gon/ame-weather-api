package br.com.amedigital.weather.api.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WaveAgitation {

    fr("Fraco"),
    mo("Moderado"),
    fo("Forte");

    String description;

    WaveAgitation(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    public static WaveAgitation fromDescription(String description) {
        for (WaveAgitation agitation : WaveAgitation.values()) {
            if (agitation.name().equals(description) || agitation.getDescription().equalsIgnoreCase(description)) {
                return agitation;
            }
        }

        return null;
    }

}
