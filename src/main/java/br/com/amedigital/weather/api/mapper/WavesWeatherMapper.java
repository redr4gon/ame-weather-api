package br.com.amedigital.weather.api.mapper;

import br.com.amedigital.weather.api.controller.response.WavesWeatherResponse;
import br.com.amedigital.weather.api.entity.WavesWeatherEntity;
import br.com.amedigital.weather.api.model.partner.response.INPEWavesWeatherCityResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class WavesWeatherMapper {

   public WavesWeatherResponse entityToResponse(WavesWeatherEntity wavesWeatherEntity) {

        WavesWeatherResponse wavesWeather = new WavesWeatherResponse();
        wavesWeather.setAgitation(wavesWeatherEntity.getAgitation());
        wavesWeather.setCityName(wavesWeatherEntity.getCityName());
        wavesWeather.setHight(wavesWeatherEntity.getHight());
        wavesWeather.setDate(wavesWeatherEntity.getDate());
        wavesWeather.setDirection(wavesWeatherEntity.getDirection());
        wavesWeather.setDirectionWind(wavesWeatherEntity.getDirectionWind());
        wavesWeather.setWind(wavesWeatherEntity.getWind());

        return wavesWeather;
    }

    public List<WavesWeatherEntity> INPEWavesWeatherCityResponseToEntity(INPEWavesWeatherCityResponse inpeWavesWeatherCityResponse, Integer code) {

        List<WavesWeatherEntity>  retorno =  inpeWavesWeatherCityResponse.getPeriodWeather().stream()
                .flatMap(pw ->  {
                    WavesWeatherEntity entity = new WavesWeatherEntity();
                    entity.setCityCode(code);
                    entity.setCityName(inpeWavesWeatherCityResponse.getName());
                    entity.setDate(getDatePeriodWavesWeather(pw));
                    entity.setAgitation(pw.getAgitation());
                    entity.setHight(pw.getHight());
                    entity.setDirection(pw.getDirection());
                    entity.setWind(pw.getWind());
                    entity.setDirectionWind(pw.getDirectionWind());
                    return Stream.of(entity);
                }).collect(Collectors.toList());

       return retorno;

    }


    private LocalDateTime getDatePeriodWavesWeather(INPEWavesWeatherCityResponse.PeriodWeather pw){
        List<String> splitDateTime = Stream.of(pw.getLocalDate().trim().split(" ")).collect(Collectors.toList());
        List<String> splitDate = Stream.of(splitDateTime.get(0).split("-")).collect(Collectors.toList());;
        Collections.reverse(splitDate);
        LocalDate datePart = LocalDate.parse(splitDate.stream().collect(Collectors.joining("-")));
        LocalTime timePart = LocalTime.parse(splitDateTime.get(1).replace("h",":00:00"));

        return LocalDateTime.of(datePart, timePart);
    }
}
