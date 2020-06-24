package br.com.amedigital.weather.api.controller;

import br.com.amedigital.weather.api.controller.request.DebtSearchRequest;
import br.com.amedigital.weather.api.controller.request.SimpleRequirementRequest;
import br.com.amedigital.weather.api.exception.PartnerException;
import br.com.amedigital.weather.api.facade.DebtFacade;
import br.com.amedigital.weather.api.mapper.DebtMapper;
import br.com.amedigital.weather.api.model.ErrorMessages;
import br.com.amedigital.weather.api.model.partner.response.ZapayDebtResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DebtControllerTest {

    private WebTestClient webTestClient;

    private DebtMapper debtMapper;

    @MockBean
    private DebtFacade debtFacade;

    @Autowired
    private DebtController debtController;

    @Before
    public void setUp(){

        debtMapper = new DebtMapper();

        webTestClient = WebTestClient
                .bindToController(new DebtController(debtFacade, debtMapper))
                .configureClient()
                .baseUrl("/debt")
                .build();
    }

    @Test
    public void shouldGetDebtsByStateWithSuccess(){

        given(debtFacade.findDebts(Mockito.any())).willReturn(Mono.just(debtMapper.zapayDebtResponseToDebtResponse(getZapayDebtResponse())));

        webTestClient.post().uri("/search")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(debtSearchRequest()), DebtSearchRequest.class))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .equals(debtMapper.zapayDebtResponseToDebtResponse(getZapayDebtResponse()));
    }

    @Test
    public void shouldGetDebtsByStateNotFound() {

        given(debtFacade.findDebts(Mockito.any())).willReturn(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));

        webTestClient.post().uri("/search")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(debtSearchRequest()), DebtSearchRequest.class))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void shouldGetDebtsByStatePartnerError() {

        given(debtFacade.findDebts(Mockito.any())).willReturn(Mono.error(new PartnerException(ErrorMessages.GENERIC_EXCEPTION, new Exception())));

        webTestClient.post().uri("/search")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(debtSearchRequest()), DebtSearchRequest.class))
                .exchange()
                .expectStatus().is5xxServerError();
    }


    private ZapayDebtResponse getZapayDebtResponse() {
        ZapayDebtResponse.Penalty penalty = new ZapayDebtResponse.Penalty();
        penalty.setAgency("agency");
        penalty.setDate(date());

        ZapayDebtResponse.DebtDetail debtDetail = new ZapayDebtResponse.DebtDetail();
        debtDetail.setAmount(new BigDecimal(12));
        debtDetail.setDescription("IPVA 2017");
        debtDetail.setType("ipva_unique");
        debtDetail.setTitle("IPVA - Cota Única - 2017");
        debtDetail.setId("RYYVN5TNTU");
        debtDetail.setPenalty(penalty);
        debtDetail.setRequired(true);
        debtDetail.setExpirationDate(date());
        debtDetail.setDueDate(date());

        List<ZapayDebtResponse.DebtDetail> debtDetails = new ArrayList<>();
        debtDetails.add(debtDetail);

        ZapayDebtResponse.Vehicle vehicle = new ZapayDebtResponse.Vehicle();
        vehicle.setRenavam("00542764407");
        vehicle.setLicensePlate("FFF9425");
        vehicle.setOwner("BRUNO TOMAZELA");
        vehicle.setDocument("397.914.818-14");
        vehicle.setChassi("chassi");
        vehicle.setModel("model");
        vehicle.setModelYear(2000);
        vehicle.setFabricationYear(2000);
        vehicle.setColor("Azul");
        vehicle.setVenalValue("venalValue");

        ZapayDebtResponse expected = new ZapayDebtResponse();
        expected.setDebts(debtDetails);
        expected.setProtocol("MNA1HFZP");
        expected.setVehicle(vehicle);
        return expected;
    }

//    private DebtResponse expectedBody() {
//        DebtResponse.Penalty penalty = new DebtResponse.Penalty();
//        penalty.setAgency("agency");
//        penalty.setDate(date());
//
//        DebtResponse.DebtDetail debtDetail = new DebtResponse.DebtDetail();
//        debtDetail.setAmount(12l);
//        debtDetail.setDescription("IPVA 2017");
//        debtDetail.setType("ipva_unique");
//        debtDetail.setId("RYYVN5TNTU");
//        debtDetail.setPenalty(penalty);
//        debtDetail.setRequired(true);
//        debtDetail.setDueDate(date());
//
//        List<DebtResponse.DebtDetail> debtDetails = new ArrayList<>();
//        debtDetails.add(debtDetail);
//
//        DebtResponse.Vehicle vehicle = new DebtResponse.Vehicle();
//        vehicle.setRenavam("00542764407");
//        vehicle.setLicensePlate("FFF9425");
//        vehicle.setOwner("BRUNO TOMAZELA");
//        vehicle.setDocument("397.914.818-14");
//        vehicle.setChassi("chassi");
//        vehicle.setModel("model");
//        vehicle.setModelYear(2000);
//        vehicle.setFabricationYear(2000);
//        vehicle.setColor("Azul");
//        vehicle.setVenalValue("venalValue");
//
//        DebtResponse expected = new DebtResponse();
//        expected.setDebts(debtDetails);
//        expected.setProtocol("MNA1HFZP");
//        expected.setVehicle(vehicle);
//        return expected;
//    }

    private LocalDate date() {
        String date = "2019-12-12 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDate.parse(date, formatter);
    }

    private DebtSearchRequest debtSearchRequest() {
        DebtSearchRequest debtSearchRequest = new DebtSearchRequest();
        debtSearchRequest.setCategoryId("bf2e071f-7324-4425-94ae-18c71e47330a");
        debtSearchRequest.setStateId("a1bb2098-06d5-4ad6-8be5-332ffaa95b7d");

        SimpleRequirementRequest requirement1 = new SimpleRequirementRequest();
        requirement1.setId("8edd6a03-7b69-4e53-8f1e-3650d7b11bfd");
        requirement1.setValue("GFA5333");

        SimpleRequirementRequest requirement2 = new SimpleRequirementRequest();
        requirement2.setId("16eb57a3-1d3c-4353-ac0f-97107c8a319d");
        requirement2.setValue("1140185184");

        List<SimpleRequirementRequest> requirements = new ArrayList<>();
        requirements.add(requirement1);
        requirements.add(requirement2);

        debtSearchRequest.setRequirements(requirements);

        return debtSearchRequest;
    }

}
