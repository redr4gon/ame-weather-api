package br.com.amedigital.weather.api.controller;

import br.com.amedigital.weather.api.controller.response.WeatherResponse;
import br.com.amedigital.weather.api.service.WeatherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findWeatherWithSuccess() {
        when(weatherService.findWeatherToCity(any())).thenReturn(Flux.just(new WeatherResponse()));

        StepVerifier.create(weatherController.findWeatherToCity("123"))
                .expectNextMatches(weatherResponse -> weatherResponse != null)
                .expectComplete()
                .verify();
    }

//    @BeforeEach
//    public void setup() {
//        schedulingRepository = mock(SchedulingRepository.class);
//        transferService = mock(TransferService.class);
//        schedulingService = spy(new SchedulingService(schedulingRepository, transferService, new Mapper));
//    }

}