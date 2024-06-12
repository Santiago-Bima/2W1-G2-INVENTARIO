package ar.edu.utn.frc.tup.lc.iv.clients;

import ar.edu.utn.frc.tup.lc.iv.configs.RestTemplateConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RestClientTest(ComprasClient.class)
@Import(RestTemplateConfig.class)
public class ComprasClientTest {
    @Autowired
    private ComprasClient comprasClient;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    void testPostRemitoId() {
        mockServer.expect(requestTo(comprasClient.getBaseResourceUrl()))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("42"));

        ResponseEntity<Long> response = comprasClient.postRemitoId(123L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(42L);

        mockServer.verify();
    }
}
