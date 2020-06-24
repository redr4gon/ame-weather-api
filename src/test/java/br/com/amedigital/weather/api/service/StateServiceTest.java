package br.com.amedigital.weather.api.service;

import br.com.amedigital.weather.api.controller.request.StateRequest;
import br.com.amedigital.weather.api.controller.response.StateResponse;
import br.com.amedigital.weather.api.entity.StateEntity;
import br.com.amedigital.weather.api.mapper.StateMapper;
import br.com.amedigital.weather.api.repository.StateRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
public class StateServiceTest {

    @Mock
    private StateRepository stateRepository;

    @Mock
    private StateMapper stateMapper;

    @InjectMocks
    public StateService stateService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(stateService);
    }

    @Test
    public void shoudFindAllWithSuccess() {
        when(stateRepository.findAll(any())).thenReturn(Flux.just(mockStateEntity()));
        when(stateMapper.stateEntityToStateResponse(any())).thenReturn(new StateResponse("1", "name"));

        StepVerifier.create(stateService.findAll(any()))
                .expectNextMatches(stateResponse -> stateResponse.getName().equals("name"))
                .expectComplete()
                .verify();
    }

    @Test
    public void shoudFindOneWithSuccess() {
        when(stateRepository.findOne("id", true)).thenReturn(Mono.just(mockStateEntity()));

        StepVerifier.create(stateService.findOne("id", true))
                .expectNextMatches(stateResponse -> stateResponse.getName().equals("name1"))
                .expectComplete()
                .verify();
    }

    @Test
    public void shoudSaveWithSuccess() {
        when(stateRepository.saveState(mockStateEntity())).thenReturn(Mono.just(mockStateEntity()));
        when(stateMapper.stateRequestToStateEntity(any())).thenReturn(mockStateEntity());

        StepVerifier.create(stateService.saveState(mockStateRequest()))
                .expectNextMatches(stateResponse -> stateResponse.getName().equals("name1"))
                .expectComplete()
                .verify();
    }

    @Test
    public void shoudActiveWithSuccess() {
        when(stateRepository.updateState(mockStateEntity())).thenReturn(Mono.just(mockStateEntity()));

        StepVerifier.create(stateService.active(mockStateEntity(), true))
                .expectComplete()
                .verify();
    }

    @Test
    public void expectInvalidNextMatchesOnFindAll() {
        when(stateRepository.findAll(any())).thenReturn(Flux.just(mockStateEntity(), anotherMockStateEntity()));
        when(stateMapper.stateEntityToStateResponse(any())).thenReturn(new StateResponse("1", "name"));


        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> StepVerifier.create(stateService.findAll(any()))
                        .expectNextMatches(stateResponse -> stateResponse.getName().equals("name1"))
                        .expectNextMatches(stateResponse -> stateResponse.getName().equals("name22"))
                        .expectComplete()
                        .verify());
    }

    private StateEntity mockStateEntity() {

        StateEntity stateEntity = new StateEntity();
        stateEntity.setId("id");
        stateEntity.setActive(true);
        stateEntity.setPartnerCode("partnerCode");
        stateEntity.setName("name1");

        return stateEntity;
    }

    private StateEntity anotherMockStateEntity() {

        StateEntity stateEntity = new StateEntity();
        stateEntity.setId("id");
        stateEntity.setActive(true);
        stateEntity.setPartnerCode("partnerCode");
        stateEntity.setName("name2");

        return stateEntity;
    }

    private StateRequest mockStateRequest() {
        StateRequest stateRequest = new StateRequest();
        stateRequest.setStateName("names");
        stateRequest.setPartnerCode("partnerCode");
        return stateRequest;
    }

}

