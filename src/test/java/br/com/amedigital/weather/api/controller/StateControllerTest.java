package br.com.amedigital.weather.api.controller;

import br.com.amedigital.weather.api.controller.response.StateResponse;
import br.com.amedigital.weather.api.entity.CategoryEntity;
import br.com.amedigital.weather.api.entity.RequirementEntity;
import br.com.amedigital.weather.api.entity.StateEntity;
import br.com.amedigital.weather.api.mapper.StateMapper;
import br.com.amedigital.weather.api.model.RequirementType;
import br.com.amedigital.weather.api.service.StateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest
public class StateControllerTest {

    private WebTestClient webTestClient;

    private StateMapper stateMapper;

    @MockBean
    private StateService stateService;

    @Autowired
    private StateController controller;

    @Before
    public void setUp(){

        stateMapper = new StateMapper();

        webTestClient = WebTestClient
                .bindToController(new StateController(stateService, stateMapper))
                .configureClient()
                .build();
    }

    @Test
    public void shouldGetAllStateWithSuccess(){

        given(stateService.findAll(new String())).willReturn(Flux.fromIterable(mockAllStateEntity()));

        webTestClient.get().uri("/state")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    public void shouldGetOneStateWithSuccess(){

        given(stateService.findOne("id", true)).willReturn(Mono.just(mockStateEntityDetail()));

        webTestClient.get().uri("/state/id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody();
    }

    private List<StateResponse> mockAllStateEntity() {

        List<StateResponse> list = new ArrayList<>();
        list.add(new StateResponse("1", "name1"));
        list.add(new StateResponse("2", "name2"));
        list.add(new StateResponse("3", "name3"));
        list.add(new StateResponse("4", "name4"));
        list.add(new StateResponse("5", "name5"));

        return list;
    }

    private StateEntity mockStateEntityDetail() {

        StateEntity stateEntity = new StateEntity();
        stateEntity.setId("id");
        stateEntity.setActive(true);
        stateEntity.setPartnerCode("partnerCode");
        stateEntity.setName("name");

        RequirementEntity requirementEntity1 = new RequirementEntity("1", "name", RequirementType.TEXT, "partnerCode", true, "id");
        RequirementEntity requirementEntity2 = new RequirementEntity("2", "name", RequirementType.TEXT, "partnerCode", true, "id");
        RequirementEntity requirementEntity3 = new RequirementEntity("3", "name", RequirementType.TEXT, "partnerCode", true, "id");

        CategoryEntity categoryEntity1 = new CategoryEntity("1", "name", "partnerCode", true, "id");
        CategoryEntity categoryEntity2 = new CategoryEntity("2", "name", "partnerCode", true, "id");
        CategoryEntity categoryEntity3 = new CategoryEntity("3", "name", "partnerCode", true, "id");

        stateEntity.addRequirements(requirementEntity1);
        stateEntity.addRequirements(requirementEntity2);
        stateEntity.addRequirements(requirementEntity3);

        stateEntity.addCategory(categoryEntity1);
        stateEntity.addCategory(categoryEntity2);
        stateEntity.addCategory(categoryEntity3);

        return stateEntity;
    }

}
