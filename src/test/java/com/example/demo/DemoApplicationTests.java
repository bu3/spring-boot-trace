package com.example.demo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.TRACE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
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
        verifyTraceFails(this.apiPort, METHOD_NOT_ALLOWED);
    }

    /**
     * Tests that TRACE is disabled for the management endpoint.
     */
    @Test
    public void testTraceDisabledManagement() throws URISyntaxException {
        verifyTraceFails(this.managementPort, FORBIDDEN);
    }

    private void verifyTraceFails(final int port, HttpStatusCode code) throws URISyntaxException {
        final String url = "http://localhost:" + port;
        RestTemplate restTemplate = new RestTemplate();
        try {
            RequestEntity<Void> requestEntity = new RequestEntity<>(TRACE, new URI(url));
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
            assertThat(response.getStatusCode(), equalTo(code));
        }
        catch (org.springframework.web.client.HttpClientErrorException e) {
            assertThat(e.getStatusCode(), equalTo(code));
            return;
        }
        catch (Exception e) {
            Assertions.fail("Should never reach this point", e);
        }
        Assertions.fail("Should never reach this point");
    }

}