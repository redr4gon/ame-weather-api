package br.com.amedigital.weather.api.mapper;

import br.com.amedigital.weather.api.controller.response.WaveResponse;
import br.com.amedigital.weather.api.entity.WaveEntity;
import br.com.amedigital.weather.api.model.WaveAgitation;
import br.com.amedigital.weather.api.model.partner.response.INPEWaveCityResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
public class WaveMapper {

    public WaveResponse entityToResponse(INPEWaveCityResponse waveCityResponse) {
        WaveResponse wave = new WaveResponse();

        wave.setCode(waveCityResponse.getCode());
        wave.setName(waveCityResponse.getName());
        wave.setState(waveCityResponse.getState());
        wave.setMorning(parseWave(waveCityResponse.getMorning()));
        wave.setAfternoon(parseWave(waveCityResponse.getAfternoon()));
        wave.setNight(parseWave(waveCityResponse.getNight()));
        wave.setUpdatedAt(waveCityResponse.getUpdatedAt());

        return wave;
    }

    private WaveResponse.Wave parseWave(INPEWaveCityResponse.Wave iWave) {
        WaveResponse.Wave wave = new WaveResponse.Wave();

        wave.setAgitation(iWave.getAgitation());
        wave.setDate(iWave.getDate());
        wave.setDirection(iWave.getDirection());
        wave.setDrift(iWave.getDrift());
        wave.setDriftDirection(iWave.getDriftDirection());
        wave.setHeight(iWave.getHeight());

        return wave;
    }

    public List<WaveEntity> INPEWaveCityResponseToEntity(INPEWaveCityResponse inpeWaveCityResponse) {
        return Arrays.asList(
                INPEWaveCityResponseToEntity(inpeWaveCityResponse, inpeWaveCityResponse.getMorning(), 'M'),
                INPEWaveCityResponseToEntity(inpeWaveCityResponse, inpeWaveCityResponse.getAfternoon(), 'A'),
                INPEWaveCityResponseToEntity(inpeWaveCityResponse, inpeWaveCityResponse.getNight(), 'N')
        );
    }

    private WaveEntity INPEWaveCityResponseToEntity(INPEWaveCityResponse inpeWaveCityResponse, INPEWaveCityResponse.Wave wave, char period) {
        WaveEntity entity = new WaveEntity();

        entity.setCityCode(inpeWaveCityResponse.getCode());
        entity.setCityName(inpeWaveCityResponse.getName());
        entity.setUpdatedAt(LocalDate.parse(inpeWaveCityResponse.getUpdatedAt(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        entity.setAgitation(WaveAgitation.fromDescription(wave.getAgitation()));
        entity.setHeight(wave.getHeight());
        entity.setDirection(wave.getDirection());
        entity.setDrift(wave.getDrift());
        entity.setDriftDirection(wave.getDriftDirection());
        entity.setPeriod(period);
        entity.setDate(wave.getDate());

        return entity;
    }

}
