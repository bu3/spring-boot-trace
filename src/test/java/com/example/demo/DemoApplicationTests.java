package com.example.demo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.TRACE;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import java.net.URI;
import java.net.URISyntaxException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "management.server.port=0")
class DemoApplicationTests {

    @LocalServerPort
    private int apiPort;

    @LocalManagementPort
    private int managementPort;

    /**
     * Tests that TRACE is disabled for the main application endpoint.
     */
    @Test
    public void testTraceDisabledApi() throws URISyntaxException {
        verifyTraceFails(this.apiPort);
    }

    /**
     * Tests that TRACE is disabled for the management endpoint.
     */
    @Test
    public void testTraceDisabledManagement() throws URISyntaxException {
        verifyTraceFails(this.managementPort);
    }

    private void verifyTraceFails(final int port) throws URISyntaxException {
        final String url = "http://localhost:" + port;
        RestTemplate restTemplate = new RestTemplate();
        try {
            RequestEntity<Void> requestEntity = new RequestEntity<>(TRACE, new URI(url));
            restTemplate.exchange(requestEntity, String.class);
        }
        catch (org.springframework.web.client.HttpClientErrorException e) {
            assertThat(e.getStatusCode(), Matchers.equalTo(METHOD_NOT_ALLOWED));
        }
    }

}