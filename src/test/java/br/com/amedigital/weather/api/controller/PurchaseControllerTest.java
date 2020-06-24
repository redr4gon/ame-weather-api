package br.com.amedigital.weather.api.controller;

import br.com.amedigital.weather.api.config.webclient.ClientException;
import br.com.amedigital.weather.api.controller.request.GenericPurchaseCallbackRequest;
import br.com.amedigital.weather.api.exception.ServiceRuntimeException;
import br.com.amedigital.weather.api.model.ErrorMessages;
import br.com.amedigital.weather.api.service.PurchaseService;
import br.com.amedigital.weather.api.utils.SupportUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
public class PurchaseControllerTest {

    private WebTestClient webTestClient;

    @Mock
    private PurchaseService purchaseService;

    @InjectMocks
    private PurchaseController purchaseController;

    @Before
    public void setUp(){

        MockitoAnnotations.initMocks(purchaseController);

        webTestClient = WebTestClient
                .bindToController(new PurchaseController(purchaseService))
                .configureClient()
                .baseUrl("/purchase")
                .build();
    }

    @Test
    public void shouldDoPaymentWithSuccess(){
        given(purchaseService.purchase(any(GenericPurchaseCallbackRequest.class))).willReturn(Mono.empty());

        webTestClient.post().uri("/callback")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(SupportUtils.getSimpleJson("PurchaseRequest.json")))
                .exchange()
                .expectStatus().isAccepted();

    }

    @Test
    public void shouldFailPaymentWithRuntimeException() throws Exception{
        given(purchaseService.purchase(any(GenericPurchaseCallbackRequest.class))).willThrow(ServiceRuntimeException.class);

        webTestClient.post().uri("/callback")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(SupportUtils.getSimpleJson("PurchaseRequest.json")))
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    public void shouldFailPaymentWithClientException(){
        given(purchaseService.purchase(any(GenericPurchaseCallbackRequest.class))).willReturn(Mono.error(new ClientException(ErrorMessages.GENERIC_EXCEPTION)));

        webTestClient.post().uri("/callback")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(SupportUtils.getSimpleJson("PurchaseRequest.json")))
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

}
