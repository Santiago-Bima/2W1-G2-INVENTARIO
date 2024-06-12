package ar.edu.utn.frc.tup.lc.iv.clients;

import ar.edu.utn.frc.tup.lc.iv.configs.RestTemplateConfig;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ProductoCatalogoDto;
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

@RestClientTest(CatalogoClient.class)
@Import(RestTemplateConfig.class)
public class CatalogoClientTest {
  @Autowired
  private CatalogoClient catalogoClient;

  @Autowired
  private MockRestServiceServer mockServer;

  @Test
  void testGetAll() {
    mockServer.expect(requestTo(catalogoClient.getBaseResourceUrl()))
            .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON));

    ResponseEntity<ProductoCatalogoDto[]> response = catalogoClient.getAll();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    mockServer.verify();
  }

  @Test
  void testGet() {
    mockServer.expect(requestTo(catalogoClient.getBaseResourceUrl() + "/2HA"))
            .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON));

    ResponseEntity<ProductoCatalogoDto> response = catalogoClient.get("2HA");

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    mockServer.verify();
  }
}
