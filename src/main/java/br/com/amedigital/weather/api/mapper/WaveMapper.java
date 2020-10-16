package br.com.amedigital.weather.api.mapper;

import br.com.amedigital.weather.api.controller.response.WaveResponse;
import br.com.amedigital.weather.api.entity.WaveEntity;
import br.com.amedigital.weather.api.model.partner.response.INPEWaveCityResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class WaveMapper {

    public WaveResponse entitytoResponse(WaveEntity waveEntity) {

        WaveResponse wave = new WaveResponse();
        wave.setId(waveEntity.getId());
        wave.setName(waveEntity.getCityName());
        wave.setUf(waveEntity.getUf());
        wave.setDate(waveEntity.getDate());
        wave.setPeriods(waveEntity.getPeriods());

        return wave;
    }

    public WaveEntity INPEWaveResponseToEntity(INPEWaveCityResponse inpeWaveCityResponse, Integer localeCode) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        WaveEntity entity = new WaveEntity();
        entity.setCityName(inpeWaveCityResponse.getName());
        entity.setUf(inpeWaveCityResponse.getUf());
        entity.setCityCode(localeCode);
        entity.setDate(LocalDate.parse(inpeWaveCityResponse.getUpdatedAt(), formatter));

        if (inpeWaveCityResponse.getMorning() != null) {
            WaveEntity.Period morning = new WaveEntity.Period();

            morning.setPeriod("manhã");
            morning.setAgitation(inpeWaveCityResponse.getMorning().getAgitation());
            morning.setHeight(inpeWaveCityResponse.getMorning().getHeight());
            morning.setDirection(inpeWaveCityResponse.getMorning().getDirection());
            morning.setWind(inpeWaveCityResponse.getMorning().getWind());
            morning.setWindDirection(inpeWaveCityResponse.getMorning().getWindDirection());

            entity.addPeriods(morning);
        }

        if (inpeWaveCityResponse.getAfternoon() != null) {
            WaveEntity.Period afternoon = new WaveEntity.Period();

            afternoon.setPeriod("tarde");
            afternoon.setAgitation(inpeWaveCityResponse.getAfternoon().getAgitation());
            afternoon.setHeight(inpeWaveCityResponse.getAfternoon().getHeight());
            afternoon.setDirection(inpeWaveCityResponse.getAfternoon().getDirection());
            afternoon.setWind(inpeWaveCityResponse.getAfternoon().getWind());
            afternoon.setWindDirection(inpeWaveCityResponse.getAfternoon().getWindDirection());

            entity.addPeriods(afternoon);
        }

        if (inpeWaveCityResponse.getNight() != null) {
            WaveEntity.Period night = new WaveEntity.Period();

            night.setPeriod("noite");
            night.setAgitation(inpeWaveCityResponse.getNight().getAgitation());
            night.setHeight(inpeWaveCityResponse.getNight().getHeight());
            night.setDirection(inpeWaveCityResponse.getNight().getDirection());
            night.setWind(inpeWaveCityResponse.getNight().getWind());
            night.setWindDirection(inpeWaveCityResponse.getNight().getWindDirection());

            entity.addPeriods(night);
        }

        return entity;
    }

}
