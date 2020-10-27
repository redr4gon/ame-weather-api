package br.com.amedigital.weather.api.service;

import br.com.amedigital.weather.api.controller.request.WeatherRequest;
import br.com.amedigital.weather.api.controller.response.WeatherResponse;
import br.com.amedigital.weather.api.entity.WeatherEntity;
import br.com.amedigital.weather.api.mapper.WeatherMapper;
import br.com.amedigital.weather.api.model.WeatherType;
import br.com.amedigital.weather.api.model.partner.response.INPECitiesResponse;
import br.com.amedigital.weather.api.model.partner.response.INPEWeatherCityResponse;
import br.com.amedigital.weather.api.repository.WeatherRepository;
import br.com.amedigital.weather.api.service.partner.INPEClientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    private WeatherRepository weatherRepository;

    @Mock
    private  WeatherMapper weatherMapper;

    @Mock
    private INPEClientService inpeClientService;

    @InjectMocks
    private WeatherService weatherService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private WeatherRequest weatherRequest = getWeatherRequest();
    private INPEWeatherCityResponse inpeWeatherCityResponse = getINPEWeatherCityResponse();
    private WeatherEntity weatherEntity = getWeatherEntity();
    private List<WeatherEntity> listWeatherEntity = getListWeatherEntity();
    private INPECitiesResponse inpeCitiesResponse = getINPECitiesResponse();
    private WeatherResponse weatherResponse = getWeatherResponse();

    @Test
    public void findWeatherToCityWithSucess() {
        when(weatherRepository.findAllCityWeather(any())).thenReturn(Flux.just(weatherEntity));
        when(inpeClientService.findWeatherToCity(any(), any())).thenReturn(Mono.just(inpeWeatherCityResponse));
        when(weatherMapper.INPEWeatherCityResponseToEntity(any(), any())).thenReturn(listWeatherEntity);
        when(weatherRepository.save(any(), any())).thenReturn(Flux.just(new WeatherEntity()));
        when(weatherMapper.entitytoResponse(any())).thenReturn(weatherResponse);

        StepVerifier.create(weatherService.findWeatherToCity(weatherRequest))
                .expectNextMatches(weatherResponse -> weatherResponse.equals(this.weatherResponse))
                .expectComplete()
                .verify();
    }

    @Test
    public void findWeatherToCityEntityWithSucess() {
        when(inpeClientService.findWeatherToCity(any(), any())).thenReturn(Mono.just(inpeWeatherCityResponse));
        when(weatherMapper.INPEWeatherCityResponseToEntity(any(), any())).thenReturn(new ArrayList<WeatherEntity>());
        when(weatherRepository.save(any(), any())).thenReturn(Flux.just(weatherEntity));

        StepVerifier.create(weatherService.findWeatherToCityEntity(weatherRequest))
                .expectNextMatches(weatherEntity -> weatherEntity.equals(this.weatherEntity))
                .expectComplete()
                .verify();
    }

    @Test
    public void findWeatherToCityEntityIntermediateWithSucess() {
        when(inpeClientService.findWeatherToCity(any(), any())).thenReturn(Mono.just(inpeWeatherCityResponse));
        when(weatherMapper.INPEWeatherCityResponseToEntity(any(), any())).thenReturn(new ArrayList<WeatherEntity>());
        when(weatherRepository.save(any(), any())).thenReturn(Flux.just(weatherEntity));

        StepVerifier.create(weatherService.findWeatherToCityEntityIntermediate(listWeatherEntity, "4"))
                .expectNextMatches(weatherEntity -> weatherEntity.equals(this.weatherEntity))
                .expectComplete()
                .verify();
    }

    @Test
    public void findWeatherToCityNameWithSucess() {
        when(inpeClientService.findCityToName(any())).thenReturn(Mono.just(inpeCitiesResponse));
        when(inpeClientService.findWeatherToCity(any(), any())).thenReturn(Mono.just(inpeWeatherCityResponse));
        when(weatherRepository.findAllCityWeather(any())).thenReturn(Flux.just(weatherEntity));
        when(weatherRepository.save(any(), any())).thenReturn(Flux.just(new WeatherEntity()));
        when(weatherMapper.entitytoResponse(any())).thenReturn(weatherResponse);

        StepVerifier.create(weatherService.findWeatherToCityName(weatherRequest))
                .expectNextMatches(weatherResponse -> weatherResponse.equals(this.weatherResponse))
                .expectComplete()
                .verify();
    }

    @Test
    public void findAllWeatherWithSucess() {
        when(weatherRepository.findAllWeather(any())).thenReturn(Flux.just(weatherEntity));
        when(weatherMapper.entitytoResponse(any())).thenReturn(weatherResponse);

        StepVerifier.create(weatherService.findAllWeather(weatherRequest))
                .expectNextMatches(weatherResponse -> weatherResponse.equals(this.weatherResponse))
                .expectComplete()
                .verify();
    }

    @Test
    public void findOneWeatherWithSucess() {
        when(weatherRepository.findOneWeather(any())).thenReturn(Mono.just(weatherEntity));
        when(weatherMapper.entitytoResponse(any())).thenReturn(weatherResponse);

        StepVerifier.create(weatherService.findOneWeather(weatherEntity.getId()))
                .expectNextMatches(weatherResponse -> weatherResponse.equals(this.weatherResponse))
                .expectComplete()
                .verify();
    }

    @Test
    public void insertWeatherWithSucess() {
        when(weatherRepository.insertWeather(any())).thenReturn(Mono.just(weatherEntity));
        when(weatherMapper.entitytoResponse(any())).thenReturn(weatherResponse);

        StepVerifier.create(weatherService.insertWeather(weatherRequest))
                .expectNextMatches(weatherResponse -> weatherResponse.equals(this.weatherResponse))
                .expectComplete()
                .verify();
    }

    @Test
    public void updateWeatherWithSucess() {
        when(weatherRepository.updateWeather(any())).thenReturn(Mono.just(weatherEntity));
        when(weatherMapper.entitytoResponse(any())).thenReturn(weatherResponse);

        StepVerifier.create(weatherService.updateWeather(weatherRequest))
                .expectNextMatches(weatherResponse -> weatherResponse.equals(this.weatherResponse))
                .expectComplete()
                .verify();
    }

    @Test
    public void deleteWeatherWithSucess() {
        when(weatherRepository.deleteWeather(any())).thenReturn(Mono.just(1));

        StepVerifier.create(weatherService.deleteWeather(weatherEntity.getId()))
                .expectNextMatches(weatherEntity -> weatherEntity == 1)
                .expectComplete()
                .verify();
    }

    private WeatherRequest getWeatherRequest() {
        WeatherRequest weatherRequest = new WeatherRequest();
        weatherRequest.setCityCode("255");
        weatherRequest.setQtDays("4");
        weatherRequest.setDate("2020-10-22");

        return weatherRequest;
    }

    private WeatherEntity getWeatherEntity() {
        WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.setId("af8c9439-bf09-4531-bf5c-1b9ecbef364d");
        weatherEntity.setMaximumTemperature(26);
        weatherEntity.setMinimumTemperature(16);
        weatherEntity.setWeather(WeatherType.valueOf("pn"));
        weatherEntity.setCityCode(255);
        weatherEntity.setCityName("Abatiá");
        weatherEntity.setDate(LocalDate.parse("2020-10-22", formatter));

        return weatherEntity;
    }

    private List<WeatherEntity> getListWeatherEntity() {
        List<WeatherEntity> weatherEntityList = new ArrayList<>();

        WeatherEntity weatherEntity20201020 = new WeatherEntity();
        weatherEntity20201020.setId("bab99a9b-a8f8-4316-bbb1-e8f1170c8e26");
        weatherEntity20201020.setMaximumTemperature(27);
        weatherEntity20201020.setMinimumTemperature(17);
        weatherEntity20201020.setWeather(WeatherType.valueOf("pt"));
        weatherEntity20201020.setCityCode(255);
        weatherEntity20201020.setCityName("Abatiá");
        weatherEntity20201020.setDate(LocalDate.parse("2020-10-20", formatter));
        weatherEntityList.add(weatherEntity20201020);

        WeatherEntity weatherEntity20201021 = new WeatherEntity();
        weatherEntity20201021.setId("08f4f991-e7ba-407b-91de-e998c3c144ad");
        weatherEntity20201021.setMaximumTemperature(29);
        weatherEntity20201021.setMinimumTemperature(18);
        weatherEntity20201021.setWeather(WeatherType.valueOf("ppt"));
        weatherEntity20201021.setCityCode(255);
        weatherEntity20201021.setCityName("Abatiá");
        weatherEntity20201021.setDate(LocalDate.parse("2020-10-21", formatter));
        weatherEntityList.add(weatherEntity20201021);

        WeatherEntity weatherEntity20201022 = getWeatherEntity();
        weatherEntityList.add(weatherEntity20201022);

        WeatherEntity weatherEntity20201023 = new WeatherEntity();
        weatherEntity20201023.setId("9eceba33-3093-416b-870f-aebc13a7437d");
        weatherEntity20201023.setMaximumTemperature(26);
        weatherEntity20201023.setMinimumTemperature(15);
        weatherEntity20201023.setWeather(WeatherType.valueOf("ps"));
        weatherEntity20201023.setCityCode(255);
        weatherEntity20201023.setCityName("Abatiá");
        weatherEntity20201023.setDate(LocalDate.parse("2020-10-23", formatter));
        weatherEntityList.add(weatherEntity20201023);

        return weatherEntityList;
    }

    private INPEWeatherCityResponse getINPEWeatherCityResponse() {
        INPEWeatherCityResponse inpeWeatherCityResponse = new INPEWeatherCityResponse();
        inpeWeatherCityResponse.setName("Abatiá");
        inpeWeatherCityResponse.setState("SP");
        inpeWeatherCityResponse.setUpdatedAt("2020-10-22");

        INPEWeatherCityResponse.Weather weather20201020 = new INPEWeatherCityResponse.Weather();
        weather20201020.setMaxTemperature(27);
        weather20201020.setMinTemperature(17);
        weather20201020.setLocalDate("2020-10-20");
        weather20201020.setWeather("pt");
        inpeWeatherCityResponse.addWeathers(weather20201020);

        INPEWeatherCityResponse.Weather weather20201021 = new INPEWeatherCityResponse.Weather();
        weather20201021.setMaxTemperature(29);
        weather20201021.setMinTemperature(18);
        weather20201021.setLocalDate("2020-10-21");
        weather20201021.setWeather("ppt");
        inpeWeatherCityResponse.addWeathers(weather20201021);

        INPEWeatherCityResponse.Weather weather20201022 = new INPEWeatherCityResponse.Weather();
        weather20201022.setMaxTemperature(26);
        weather20201022.setMinTemperature(16);
        weather20201022.setLocalDate("2020-10-22");
        weather20201022.setWeather("pn");
        inpeWeatherCityResponse.addWeathers(weather20201022);

        INPEWeatherCityResponse.Weather weather20201023 = new INPEWeatherCityResponse.Weather();
        weather20201023.setMaxTemperature(26);
        weather20201023.setMinTemperature(15);
        weather20201023.setLocalDate("2020-10-23");
        weather20201023.setWeather("ps");
        inpeWeatherCityResponse.addWeathers(weather20201023);


        return inpeWeatherCityResponse;
    }

    private INPECitiesResponse getINPECitiesResponse() {
        INPECitiesResponse inpeCitiesResponse = new INPECitiesResponse();

        INPECitiesResponse.City city = new INPECitiesResponse.City();
        city.setCityCode(255);
        city.setName("Abatiá");
        city.setState("SP");

        inpeCitiesResponse.addCities(city);

        return inpeCitiesResponse;
    }

    private WeatherResponse getWeatherResponse() {
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setId("af8c9439-bf09-4531-bf5c-1b9ecbef364d");
        weatherResponse.setMaximumTemperature(26);
        weatherResponse.setMinimumTemperature(16);
        weatherResponse.setWeather(WeatherType.valueOf("pn"));
        weatherResponse.setWeatherCity("Abatiá");
        weatherResponse.setWeatherDate(LocalDate.parse("2020-10-22", formatter));

        return weatherResponse;
    }
}
