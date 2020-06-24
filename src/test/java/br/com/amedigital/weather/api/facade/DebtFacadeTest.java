package br.com.amedigital.weather.api.facade;

import br.com.amedigital.weather.api.controller.request.DebtSearchRequest;
import br.com.amedigital.weather.api.controller.request.SimpleRequirementRequest;
import br.com.amedigital.weather.api.entity.CategoryEntity;
import br.com.amedigital.weather.api.entity.RequirementEntity;
import br.com.amedigital.weather.api.entity.StateEntity;
import br.com.amedigital.weather.api.service.CategoryService;
import br.com.amedigital.weather.api.service.RequirementService;
import br.com.amedigital.weather.api.service.StateService;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
public class DebtFacadeTest {

    @InjectMocks
    private DebtFacade debtFacade;

    @Mock
    private StateService stateService;

    @Mock
    private RequirementService requirementService;

    @Mock
    private CategoryService categoryService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(debtFacade);
    }

    @Test
    public void shouldPutStateWithSuccess() {
        when(stateService.findOne("a1bb2098-06d5-4ad6-8be5-332ffaa95b7d", true)).thenReturn(Mono.just(mockStateEntity()));

        StepVerifier.create(debtFacade.putState(debtSearchRequest(), new JSONObject()))
                .expectNextMatches(jsonObject -> jsonObject.get("state") == "SP")
                .expectComplete()
                .verify();
    }

    @Test
    public void shouldPutStateWithError() {
        when(stateService.findOne("a1bb2098-06d5-4ad6-8be5-332ffaa95b7d", true)).thenReturn(Mono.just(mockStateEntity()));

        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> StepVerifier.create(debtFacade.putState(debtSearchRequest(), new JSONObject()))
                .expectNextMatches(jsonObject -> jsonObject.get("state") == "SPP")
                .expectComplete()
                .verify());
    }

    @Test
    public void shouldPutCategoryWithSuccess() {
        when(categoryService.findOne("bf2e071f-7324-4425-94ae-18c71e47330a")).thenReturn(Mono.just(mockCategoryEntity()));

        StepVerifier.create(debtFacade.putCategory(debtSearchRequest(), new JSONObject()))
                .expectNextMatches(jsonObject -> jsonObject.get("debt_option") == "debts")
                .expectComplete()
                .verify();
    }

    @Test
    public void shouldPutCategoryWithError() {
        when(categoryService.findOne("bf2e071f-7324-4425-94ae-18c71e47330a")).thenReturn(Mono.just(mockCategoryEntity()));

        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> StepVerifier.create(debtFacade.putCategory(debtSearchRequest(), new JSONObject()))
                .expectNextMatches(jsonObject -> jsonObject.get("debt_option") == "debt")
                .expectComplete()
                .verify());
    }

    @Test
    public void shouldPutAllRequirementsWithSuccess() {
        when(requirementService.findOne(Mockito.eq("8edd6a03-7b69-4e53-8f1e-3650d7b11bfd"))).thenReturn(Mono.just(mockRequirementEntity1()));
        when(requirementService.findOne(Mockito.eq("16eb57a3-1d3c-4353-ac0f-97107c8a319d"))).thenReturn(Mono.just(mockRequirementEntity2()));

        StepVerifier.create(debtFacade.putAllRequirements(debtSearchRequest(), new JSONObject()))
                .expectNextMatches(jsonObject -> jsonObject.get("licence_plate") == "GFA5333" && jsonObject.get("renavam") == "1140185184")
                .expectComplete()
                .verify();
    }

    @Test
    public void shouldPutAllRequirementsWithError() {
        when(requirementService.findOne(Mockito.eq("8edd6a03-7b69-4e53-8f1e-3650d7b11bfd"))).thenReturn(Mono.just(mockRequirementEntity1()));
        when(requirementService.findOne(Mockito.eq("16eb57a3-1d3c-4353-ac0f-97107c8a319d"))).thenReturn(Mono.just(mockRequirementEntity2()));

        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> StepVerifier.create(debtFacade.putAllRequirements(debtSearchRequest(), new JSONObject()))
                .expectNextMatches(jsonObject -> jsonObject.get("licence_plate") == "GFA53334" && jsonObject.get("renavam") == "11401851848")
                .expectComplete()
                .verify());
    }

    private StateEntity mockStateEntity() {
        StateEntity stateEntity = new StateEntity();
        stateEntity.setId("id");
        stateEntity.setActive(true);
        stateEntity.setPartnerCode("SP");
        stateEntity.setName("name");
        return stateEntity;
    }

    private CategoryEntity mockCategoryEntity() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId("bf2e071f-7324-4425-94ae-18c71e47330a");
        categoryEntity.setRequired(true);
        categoryEntity.setName("name");
        categoryEntity.setStateId("a1bb2098-06d5-4ad6-8be5-332ffaa95b7d");
        categoryEntity.setPartnerCode("debts");
        return categoryEntity;
    }

    private RequirementEntity mockRequirementEntity1() {
        RequirementEntity requirementEntity = new RequirementEntity();
        requirementEntity.setId("8edd6a03-7b69-4e53-8f1e-3650d7b11bfd");
        requirementEntity.setStateId("a1bb2098-06d5-4ad6-8be5-332ffaa95b7d");
        requirementEntity.setName("name");
        requirementEntity.setPartnerCode("licence_plate");
        requirementEntity.setRequired(true);
        return requirementEntity;
    }

    private RequirementEntity mockRequirementEntity2() {
        RequirementEntity requirementEntity = new RequirementEntity();
        requirementEntity.setId("16eb57a3-1d3c-4353-ac0f-97107c8a319d");
        requirementEntity.setStateId("a1bb2098-06d5-4ad6-8be5-332ffaa95b7d");
        requirementEntity.setName("name");
        requirementEntity.setPartnerCode("renavam");
        requirementEntity.setRequired(true);
        return requirementEntity;
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