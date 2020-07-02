package br.com.amedigital.weather.api.exception.messages;

import br.com.amedigital.weather.api.model.ErrorMessages;

public class CityErrorMessages extends ErrorMessages {

    public static final CityErrorMessages CITY_NOT_FOUND = new CityErrorMessages("city.notFound");

    public static final CityErrorMessages MANY_CITIES_FOUND = new CityErrorMessages("city.manyCitiesFound");

    protected CityErrorMessages(int aMessageID, String aKey) {
        super(aMessageID, aKey);
    }

    protected CityErrorMessages(String aKey) {
        super(aKey);
    }

}
