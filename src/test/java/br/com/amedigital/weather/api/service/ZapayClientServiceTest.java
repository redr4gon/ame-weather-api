package br.com.amedigital.weather.api.service;

import br.com.amedigital.weather.api.model.partner.response.ZapayDebtResponse;
import br.com.amedigital.weather.api.service.partner.INPEClientService;
import br.com.amedigital.weather.api.utils.SupportUtils;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


public class ZapayClientServiceTest {

    private static final Logger LOG = getLogger(ZapayClientServiceTest.class);

    private static final String zapayToken = SupportUtils.getSimpleJson("ZapayTokenResponse.json");

    private static final String zapayFindDebt = SupportUtils.getSimpleJson("ZapayDebtResponse.json");

    private INPEClientService zapayClientService;

    private MockWebServer mockWebServer;

    @BeforeEach
    public void setup() throws IOException {
        mockWebServer = new MockWebServer();
        final Dispatcher dispatcher = new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                switch (request.getPath()) {
                    case "/authentication/":
                        return new MockResponse().setResponseCode(200).setBody(zapayToken).addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    case "/zapi/debts/":
                        return new MockResponse().setResponseCode(200).setBody(zapayFindDebt).addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                }
                return new MockResponse().setResponseCode(400);
            }
        };
        mockWebServer.setDispatcher(dispatcher);
        mockWebServer.start();
        zapayClientService = new INPEClientService(WebClient.builder().clientConnector(new ReactorClientHttpConnector()).build(),
                new ZapayClientServiceConfigStub(), mockWebServer.url("/").toString());
    }

    @AfterEach
    void afterTests() {
        try {
            LOG.warn("=== Web Client Mock Finishing ===");
            mockWebServer.shutdown();
        } catch (Exception e) {
            LOG.warn("=== ERROR - Cant finish mock web server from tests ===", e);
        }
    }

    @Test
    public void testGetTokenOk() throws IOException {

        StepVerifier.create(zapayClientService.generateToken())
                .expectNext("JWT token")
                .verifyComplete();

    }

    @Test
    public void testFindDebtsOk() {
        ZapayDebtResponse expected = getZapayDebtResponse();
        Mono<ZapayDebtResponse> zapayDebtResponse = zapayClientService.findDebtsByState(debtSearchRequest());
        StepVerifier.create(zapayDebtResponse)
                .expectNext(expected)
                .verifyComplete();
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

    private LocalDate date() {
        String date = "2019-12-12 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDate.parse(date, formatter);
    }

    private JSONObject debtSearchRequest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("stateId", "SP");
        jsonObject.put("licensePlate", "FFF9425");
        jsonObject.put("renavam", "00542764407");
        return jsonObject;
    }
}