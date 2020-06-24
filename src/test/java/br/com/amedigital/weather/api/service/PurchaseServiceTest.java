package br.com.amedigital.weather.api.service;

import br.com.amedigital.weather.api.controller.request.GenericPurchaseCallbackRequest;
import br.com.amedigital.weather.api.controller.request.PurchaseCallbackRequest;
import br.com.amedigital.vehicle.debts.api.entity.*;
import br.com.amedigital.weather.api.facade.StateFacade;
import br.com.amedigital.weather.api.mapper.DebtMapper;
import br.com.amedigital.weather.api.mapper.PurchaseCallbackMapper;
import br.com.amedigital.weather.api.model.DebtType;
import br.com.amedigital.weather.api.model.WalletOrderStatus;
import br.com.amedigital.weather.api.model.partner.ame.response.CustomerDetailResponse;
import br.com.amedigital.weather.api.model.partner.ame.response.OrderDetailResponse;
import br.com.amedigital.weather.api.model.partner.request.PartnerPaymentRequest;
import br.com.amedigital.weather.api.model.partner.response.ZapayPaymentCheckResponse;
import br.com.amedigital.weather.api.model.partner.response.INPEWeatherCityResponse;
import br.com.amedigital.weather.api.repository.DebtRepository;
import br.com.amedigital.weather.api.repository.PaymentOrderDetailRepository;
import br.com.amedigital.weather.api.service.ame.CustomerClientService;
import br.com.amedigital.weather.api.service.ame.OrderClientService;
import br.com.amedigital.weather.api.service.ame.PurchaseClientService;
import br.com.amedigital.weather.api.service.partner.INPEClientService;
import br.com.amedigital.weather.api.utils.SupportUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@RunWith(MockitoJUnitRunner.class)
public class PurchaseServiceTest {

    @InjectMocks
    private PurchaseService purchaseService;

    @Mock
    private PaymentOrderDetailRepository paymentOrderDetailRepository;

    @Mock
    private PurchaseClientService purchaseClient;

    protected ObjectMapper objectMapper;

    @Mock
    private PurchaseCallbackMapper purchaseOrderCallbackMapper;

    @Mock
    private OrderClientService orderClient;

    @Mock
    private CustomerClientService customerClient;

    @Mock
    private INPEClientService zapayClientService;

    @Mock
    private StateFacade stateFacade;

    @Mock
    private PaymentOrderDetailEntity detailEntity;

    @Mock
    private RequirementService requirementService;

    @Mock
    private DebtRepository debtRepository;

    @Mock
    private DebtMapper debtMapper;

    @Before
    public void setUp(){
        ReflectionTestUtils.setField(purchaseService, "retryableConfig", new RetryableConfigStub());
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        MockitoAnnotations.initMocks(purchaseService);
    }

    @Test
    public void purchaseSuccess() {

        PaymentOrderEntity paymentOrder = getPaymentOrderDetail();
        OrderDetailResponse orderDetail = SupportUtils.getPojo("AuthorizedOrderDetail.json", OrderDetailResponse.class);

        given(purchaseOrderCallbackMapper.requestToEntity(any())).willReturn(getPaymentOrderDetail());
        given(purchaseOrderCallbackMapper.entityToZapayPaymentRequest(any())).willReturn(partnerPaymentRequest());
        given(paymentOrderDetailRepository.save(any())).willReturn(Mono.just(paymentOrder));
        given(paymentOrderDetailRepository.update(any())).willReturn(Mono.just(getPaymentOrderDetail()));
        given(customerClient.getCustomer(any())).willReturn(Mono.just(customerDetailResponse()));
        given(orderClient.getOrderDetail(any())).willReturn(Mono.just(orderDetail));
        given(orderClient.validateOrder(any())).willReturn(Mono.just(orderDetail));
        given(zapayClientService.payment(any())).willReturn(Mono.just(SupportUtils.getPojo("PaymentResponse.json", INPEWeatherCityResponse.class)));
        given(zapayClientService.checkPayment(any())).willReturn(Mono.just(SupportUtils.getPojo("PaymentCheckResponse.json", ZapayPaymentCheckResponse.class)));
        given(stateFacade.findOneState(any())).willReturn(Mono.just(stateEntity()));
        given(debtRepository.findAll(any(), any())).willReturn(Flux.just(debtEntity()));
        given(debtMapper.debtEntityToPaymentDebtEntity(any())).willReturn(paymentOrderDebtEntity());

        StepVerifier.create(purchaseService.purchase(purchaseGenericRequest()))
            .expectComplete()
            .verify();
    }

    private CustomerDetailResponse customerDetailResponse() {
        CustomerDetailResponse customerDetailResponse = new CustomerDetailResponse();
        customerDetailResponse.setEmail("email@email.com");
        customerDetailResponse.setId("customerId");
        customerDetailResponse.setName("name");
        return customerDetailResponse;
    }

    private GenericPurchaseCallbackRequest purchaseGenericRequest() {

        try {
            return objectMapper.readValue(SupportUtils.getSimpleJson("PurchaseRequest.json"),
                            new TypeReference<GenericPurchaseCallbackRequest<PurchaseCallbackRequest>>() {});
        }catch (Exception e){
            return null;
        }
    }

    private PaymentOrderDetailEntity getPaymentOrderDetail() {

        CustomerDetailResponse customerDetailResponse = customerDetailResponse();
        PaymentOrderDetailEntity paymentOrderDetail = new PaymentOrderDetailEntity();

        PaymentOrderDebtEntity debtEntity = new PaymentOrderDebtEntity();
        debtEntity.setDueDate(LocalDate.now().plusMonths(1));
        debtEntity.setAuthenticationCode("auth");
        debtEntity.setType(DebtType.INSURANCE.getDescription());
        debtEntity.setOrderId("orderId");
        debtEntity.setDescription("description");
        debtEntity.setAmount(2000l);
        debtEntity.setPartnerId("partnerId");

        paymentOrderDetail.setRenavam("123456789");
        paymentOrderDetail.setLicensePlate("ABC1234");
        paymentOrderDetail.setCustomerEmail(customerDetailResponse.getEmail());
        paymentOrderDetail.setCustomerName(customerDetailResponse.getName());
        paymentOrderDetail.setCustomerId(customerDetailResponse.getId());
        paymentOrderDetail.setOrderDetailId("detailId");
        paymentOrderDetail.setWalletId("walletId");
        paymentOrderDetail.setOrderId("orderId");
        paymentOrderDetail.setWalletStatus(WalletOrderStatus.AUTHORIZED);
        List<PaymentOrderDebtEntity> entities = new ArrayList<>();
        paymentOrderDetail.setDebtEntities(entities);
        paymentOrderDetail.setStatePartnerCode("statePartnerCode");
        paymentOrderDetail.setStateId("stateId");
        paymentOrderDetail.setProtocol("protocol");
        paymentOrderDetail.setCustomPayload(new ObjectMapper().convertValue(purchaseGenericRequest().getCustomPayload(PurchaseCallbackRequest.class), Map.class));

        return paymentOrderDetail;
    }

    private StateEntity stateEntity() {
        StateEntity stateEntity = new StateEntity();
        stateEntity.setName("state");
        stateEntity.setPartnerCode("code");
        stateEntity.setId("state");
        return stateEntity;
    }

    private DebtEntity debtEntity() {
        DebtEntity debtEntity = new DebtEntity();
        debtEntity.setType("ipva");
        debtEntity.setPartnerProtocol("protocol");
        debtEntity.setPartnerDebtId("id");
        debtEntity.setDueDate(LocalDate.now());
        debtEntity.setDescription("IPVA 2020");
        debtEntity.setAmount(2000l);
        debtEntity.setCreatedAt(LocalDateTime.now());
        return debtEntity;
    }

    private PartnerPaymentRequest partnerPaymentRequest() {
        PartnerPaymentRequest partnerPaymentRequest = new PartnerPaymentRequest();
        partnerPaymentRequest.setProtocol("protocol");
        partnerPaymentRequest.setPartnerState("partnerState");
        partnerPaymentRequest.setDebtIdList(Arrays.asList("id"));
        return partnerPaymentRequest;
    }

    private PaymentOrderDebtEntity paymentOrderDebtEntity() {
        PaymentOrderDebtEntity paymentOrderDebtEntity = new PaymentOrderDebtEntity();
        paymentOrderDebtEntity.setType("ipva");
        paymentOrderDebtEntity.setPartnerId("id");
        paymentOrderDebtEntity.setDueDate(LocalDate.now());
        paymentOrderDebtEntity.setDescription("IPVA 2020");
        paymentOrderDebtEntity.setAmount(2000l);
        return paymentOrderDebtEntity;
    }

}
