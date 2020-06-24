package br.com.amedigital.weather.api.utils;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import org.slf4j.Logger;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * WebClientMock to create test server
 */
public class WebClientMock {

    private static final Logger LOG = getLogger(WebClientMock.class);
    private MockWebServer mockWebServer;
    private MockResponse mockResponse;

    public WebClientMock() {
        try {
            LOG.warn("=== Web Client Mock Starting ===");
            mockResponse = new MockResponse();
            mockWebServer = new MockWebServer();
            mockWebServer.start();
        } catch (Exception e) {
            LOG.warn("=== ERROR - Cant start mock web server to tests ===", e);
        }
    }

    /**
     * Use it after tests, to free the server socket
     */
    public void shutdown() {
        try {
            LOG.warn("=== Web Client Mock Finishing ===");
            mockWebServer.shutdown();
        } catch (Exception e) {
            LOG.warn("=== ERROR - Cant finish mock web server from tests ===", e);
        }
    }

    /**
     * Use it after configurations but before the mock call
     */
    public void start() {
        if (mockResponse.getHeaders().size() == 0 || mockResponse.getHeaders().get(CONTENT_TYPE) == null) {
            mockResponse.addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        }
        mockWebServer.enqueue(mockResponse);
    }

    public UriComponents getUri() {
        return UriComponentsBuilder.fromUriString(mockWebServer.url("/").toString()).build();
    }

    public void setResponseCode(int responseCode) {
        mockResponse.setResponseCode(responseCode);
    }

    public void setResponseBody(String body) {
        mockResponse.setBody(body);
    }

    public void setResponseHeader(Map<String, String> header) {
        header.forEach((k, v) -> mockResponse.addHeader(k, v));
    }

    public void addResponseHeader(String name, String value) {
        mockResponse.addHeader(name, value);
    }
}
