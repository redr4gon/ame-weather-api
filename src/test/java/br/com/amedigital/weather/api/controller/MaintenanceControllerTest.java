package br.com.amedigital.weather.api.controller;

import br.com.amedigital.weather.api.controller.request.CategoryRequest;
import br.com.amedigital.weather.api.controller.request.RequirementRequest;
import br.com.amedigital.weather.api.controller.request.StateRequest;
import br.com.amedigital.weather.api.controller.response.CategoryResponse;
import br.com.amedigital.weather.api.controller.response.RequirementResponse;
import br.com.amedigital.weather.api.controller.response.StateDetailResponse;
import br.com.amedigital.weather.api.entity.CategoryEntity;
import br.com.amedigital.weather.api.entity.RequirementEntity;
import br.com.amedigital.weather.api.entity.StateEntity;
import br.com.amedigital.weather.api.exception.NotFoundException;
import br.com.amedigital.weather.api.facade.StateFacade;
import br.com.amedigital.weather.api.mapper.CategoryMapper;
import br.com.amedigital.weather.api.mapper.RequirementMapper;
import br.com.amedigital.weather.api.mapper.StateMapper;
import br.com.amedigital.weather.api.model.ErrorMessages;
import br.com.amedigital.weather.api.model.RequirementType;
import br.com.amedigital.weather.api.service.CategoryService;
import br.com.amedigital.weather.api.service.RequirementService;
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
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MaintenanceControllerTest {

    private WebTestClient webTestClient;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private StateService stateService;

    @MockBean
    private RequirementService requirementService;

    @MockBean
    private StateFacade stateFacade;

    @Autowired
    private MaintenanceController controller;

    private RequirementMapper requirementMapper;

    private CategoryMapper categoryMapper;

    @Before
    public void setUp(){

        webTestClient = WebTestClient
                .bindToController(new MaintenanceController(stateService, stateFacade, requirementService, categoryService))
                .configureClient()
                .baseUrl("/maintenance")
                .build();
    }

    @Test
    public void shouldActiveWithSuccess(){
        given(stateFacade.validateState("id", false)).willReturn(Mono.just(mockStateEntity(false)));
        given(stateService.active(mockStateEntity(false), true)).willReturn(Mono.empty());

        webTestClient.put().uri(String.format("/state/active/%s?state-id=%s", true, "id"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    public void shouldNotValidStateOnActive(){

        String stateId = "1";
        boolean active = true;

        given(stateFacade.validateState(stateId, true)).willReturn(Mono.error(new NotFoundException(ErrorMessages.STATE_NOT_FOUND)));

        webTestClient.put().uri(String.format("/state/active/%s?state-id=%s", active, stateId))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    public void shouldCreateState(){
        given(stateFacade.saveState(any())).willReturn(Mono.just(mockStateEntityDetail()));

        webTestClient.post().uri("/state")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(mockStateRequest()), StateRequest.class))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(StateDetailResponse.class);
    }

    @Test
    public void shouldCreateRequirement(){
        given(stateFacade.validateState("id", true)).willReturn(Mono.just(mockStateEntity(true)));
        given(requirementService.saveOne(any())).willReturn(mockRequirementResponse());


        webTestClient.post().uri("/requirement")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(mockRequirementRequest()), RequirementRequest.class))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(RequirementResponse.class);
    }

    @Test
    public void shouldCreateCategory(){
        given(stateFacade.validateState("id", true)).willReturn(Mono.just(mockStateEntity(true)));
        given(categoryService.saveOne(any())).willReturn(mockCategoryResponse());

        webTestClient.post().uri("/category")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(mockCategoryRequest()), CategoryRequest.class))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(CategoryResponse.class);
    }

    private StateEntity mockStateEntity(boolean active) {

        StateEntity stateEntity = new StateEntity();
        stateEntity.setId("id");
        stateEntity.setActive(active);
        stateEntity.setPartnerCode("partnerCode");
        stateEntity.setName("name");

        return stateEntity;
    }



    private StateRequest mockStateRequest() {
        StateRequest stateRequest = new StateRequest();
        stateRequest.setStateName("name");
        stateRequest.setPartnerCode("partnerCode");
        return stateRequest;
    }

    private StateDetailResponse mockStateEntityDetail() {

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

        return new StateMapper().stateEntityToStateDetailResponse(stateEntity);
    }

    private Flux<RequirementResponse> mockRequirementResponse() {
        return Flux.just(new RequirementResponse("1", "requirements", "TEXT", true));
    }

    private RequirementRequest mockRequirementRequest() {
        RequirementRequest.Requirement req1 = new RequirementRequest.Requirement();
        req1.setName("name1");
        req1.setPartnerCode("partnerCode1");
        req1.setRequired(true);
        req1.setType(RequirementType.TEXT);

        RequirementRequest.Requirement req2 = new RequirementRequest.Requirement();
        req2.setName("name2");
        req2.setPartnerCode("partnerCode2");
        req2.setRequired(true);
        req2.setType(RequirementType.NUMERIC);

        RequirementRequest requirementRequest = new RequirementRequest();
        requirementRequest.setStateId("id");
        requirementRequest.setRequirements(Arrays.asList(req1, req2));

        return requirementRequest;
    }

    private Flux<CategoryResponse> mockCategoryResponse() {
        return Flux.just(new CategoryResponse("1", "category", true));
    }

    private CategoryRequest mockCategoryRequest() {
        CategoryRequest.Category cat1 = new CategoryRequest.Category();
        cat1.setName("name1");
        cat1.setPartnerCode("partnerCode1");
        cat1.setRequired(true);

        CategoryRequest.Category cat2 = new CategoryRequest.Category();
        cat2.setName("name2");
        cat2.setPartnerCode("partnerCode2");
        cat2.setRequired(true);

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setStateId("id");
        categoryRequest.setCategories(Arrays.asList(cat1, cat2));

        return categoryRequest;
    }
}
