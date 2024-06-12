package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.response.ZonaAlmacenamientoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ZonaControllerTest{
  @LocalServerPort
  private int port;
  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void testGetEndpoint() {
    String url = "http://localhost:" + port + "/inventario/zonas/";
    ResponseEntity<List<ZonaAlmacenamientoResponse>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ZonaAlmacenamientoResponse>>() {});
    List<ZonaAlmacenamientoResponse> zonas = response.getBody();
    System.out.println(zonas);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(false, zonas.isEmpty());
  }
}

