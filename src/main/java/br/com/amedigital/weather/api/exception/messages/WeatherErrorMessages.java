package br.com.amedigital.weather.api.exception.messages;

import br.com.amedigital.weather.api.model.ErrorMessages;

public class WeatherErrorMessages extends ErrorMessages {

    public static final WeatherErrorMessages WEATHER_NOT_FOUND = new WeatherErrorMessages("weather.notFound");

    protected WeatherErrorMessages(int aMessageID, String aKey) {
        super(aMessageID, aKey);
    }

    protected WeatherErrorMessages(String aKey) {
        super(aKey);
    }

}
