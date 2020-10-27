package br.com.amedigital.weather.api.controler;

import br.com.amedigital.weather.api.controller.WeatherController;
import br.com.amedigital.weather.api.controller.request.WeatherRequest;
import br.com.amedigital.weather.api.controller.response.WeatherResponse;
import br.com.amedigital.weather.api.model.WeatherType;
import br.com.amedigital.weather.api.service.WeatherService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherControlerTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private WeatherRequest weatherRequest = new WeatherRequest("af8c9439-bf09-4531-bf5c-1b9ecbef364d", "4", "40", "26","pc", "255", "Abatiá", "2020-10-22");
    WeatherResponse weatherResponse =  getWeatherResponse();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findWeatherToCityWithSucess() {
        when(weatherService.findWeatherToCity(any())).thenReturn(Flux.just(weatherResponse));

        StepVerifier.create(weatherController.findWeatherToCity(weatherRequest))
                .expectNextMatches(weatherResponse -> weatherResponse.equals(this.weatherResponse))
                .expectComplete()
                .verify();
    }

    @Test
    public void findWeatherToCityNameWithSucess() {
        when(weatherService.findWeatherToCityName(any())).thenReturn(Flux.just(weatherResponse));

        StepVerifier.create(weatherController.findWeatherToCityName(weatherRequest.getCityName(), Integer.valueOf(weatherRequest.getQtDays())))
                .expectNextMatches(weatherResponse -> weatherResponse.equals(this.weatherResponse))
                .expectComplete()
                .verify();
    }

    @Test
    public void findAllWeatherWithSucess() {
        when(weatherService.findAllWeather(any())).thenReturn(Flux.just(weatherResponse));

        StepVerifier.create(weatherController.findAllWeather(weatherRequest))
                .expectNextMatches(weatherResponse -> weatherResponse.equals(this.weatherResponse))
                .expectComplete()
                .verify();
    }

    @Test
    public void findOneWeather() {
        when(weatherService.findOneWeather(any())).thenReturn(Mono.just(weatherResponse));

        StepVerifier.create(weatherController.findOneWeather(weatherRequest.getId()))
                .expectNextMatches(weatherResponse -> weatherResponse.equals(this.weatherResponse))
                .expectComplete()
                .verify();
    }

    @Test
    public void insertWeather() {
        when(weatherService.insertWeather(any())).thenReturn(Mono.just(weatherResponse));

        StepVerifier.create(weatherController.insertWeather(weatherRequest))
                .expectNextMatches(weatherResponse -> weatherResponse.equals(this.weatherResponse))
                .expectComplete()
                .verify();
    }

    @Test
    public void updateWeather() {
        when(weatherService.updateWeather(any())).thenReturn(Mono.just(weatherResponse));

        StepVerifier.create(weatherController.updateWeather(weatherRequest))
                .expectNextMatches(weatherResponse -> weatherResponse.equals(this.weatherResponse))
                .expectComplete()
                .verify();
    }

    @Test
    public void deleteWeather() {
        when(weatherService.deleteWeather(any())).thenReturn(Mono.just(1));

        StepVerifier.create(weatherController.deleteWeather(weatherRequest.getId()))
                .expectNextMatches(integer -> integer == 1)
                .expectComplete()
                .verify();
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
