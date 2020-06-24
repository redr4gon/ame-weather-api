package br.com.amedigital.weather.api.service.commons;

import br.com.amedigital.weather.api.model.ErrorMessages;
import br.com.amedigital.weather.api.exception.ServiceException;
import br.com.amedigital.weather.api.model.CommonMessages;
import br.com.amedigital.weather.api.repository.CommonMessagesRepository;
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
public class CommonMessagesServiceTest {

    private final String NOT_FOUND_MESSAGE = "Não foi possível localizar a propriedade com o nome: voucher.howToRedeem";

    @InjectMocks
    private CommonMessagesService testClass;

    @Mock
    private CommonMessagesRepository commonMessagesRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(testClass);
    }

    @Test
    public void getHowToRedeem() {
        given(commonMessagesRepository.findByName("voucher.howToRedeem")).willReturn(Mono.just(getHowToRedeemCommonMessage()));

        StepVerifier.create(testClass.getHowToRedeem())
                .assertNext(commonMessages -> assertEquals("howTo", commonMessages.getName()))
                .verifyComplete();
    }

    @Test
    public void getTerms() {
        given(commonMessagesRepository.findByName("voucher.terms")).willReturn(Mono.just(getTermsCommonMessage()));

        StepVerifier.create(testClass.getTerms())
                .assertNext(commonMessages -> assertEquals("terms", commonMessages.getName()))
                .verifyComplete();
    }

    @Test
    public void getMessageNotFound() {
        given(commonMessagesRepository.findByName("voucher.howToRedeem")).willReturn(Mono.justOrEmpty(any()));
        given(testClass.findByName("voucher.howToRedeem")).willReturn(Mono.error(new ServiceException(ErrorMessages.FIND_BY_NAME_NOT_FOUND, "voucher.howToRedeem")));

        Mono<CommonMessages> test = testClass.getHowToRedeem();

        StepVerifier.create(test)
                .expectErrorMatches(throwable -> throwable instanceof ServiceException
                        && NOT_FOUND_MESSAGE.equals(throwable.getMessage()))
                .verify();
    }


    private CommonMessages getHowToRedeemCommonMessage() {
        return new CommonMessages("howTo", "value", "description");
    }

    private CommonMessages getTermsCommonMessage() {
        return new CommonMessages("terms", "value", "description");
    }
}
