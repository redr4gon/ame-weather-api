package br.com.amedigital.weather.api.service.commons;

import br.com.amedigital.weather.api.model.ErrorMessages;
import br.com.amedigital.weather.api.exception.ServiceException;
import br.com.amedigital.weather.api.model.CommonProperties;
import br.com.amedigital.weather.api.repository.CommonPropertiesRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class CommonPropertiesServiceTest {

    private final String NOT_FOUND_MESSAGE = "Não foi possível localizar a propriedade com o nome: cities.available";

    @InjectMocks
    private CommonPropertiesService testClass;

    @Mock
    private CommonPropertiesRepository commonPropertiesRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(testClass);
    }

    @Test
    public void findByName() {
        given(commonPropertiesRepository.findByName("cities.available")).willReturn(Mono.just(getCitiesProperties()));

    StepVerifier.create(testClass.findByName("cities.available"))
                .assertNext(commonMessages -> assertEquals("cities.available", commonMessages.getName()))
                .verifyComplete();
    }

    @Test
    public void getPropertiesNotFound() {
        given(commonPropertiesRepository.findByName("cities.available")).willReturn(Mono.justOrEmpty(any()));
        given(testClass.findByName("cities.available")).willReturn(Mono.error(new ServiceException(ErrorMessages.FIND_BY_NAME_NOT_FOUND, "cities.available")));

        Mono<CommonProperties> test = testClass.findByName("cities.available");

        StepVerifier.create(test)
                .expectErrorMatches(throwable -> throwable instanceof ServiceException
                        && NOT_FOUND_MESSAGE.equals(throwable.getMessage()))
                .verify();
    }


    private CommonProperties getCitiesProperties() {
        return new CommonProperties("cities.available", "SPO", "description");
    }
}
