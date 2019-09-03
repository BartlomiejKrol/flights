package krol.flights.routes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class RoutesClient {

    private static final String ROUTES_URL = "https://services-api.ryanair.com/locate/3/routes";
    private RestTemplate restTemplate;

    @Autowired
    public RoutesClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Route> fetchRoutes() {
        ResponseEntity<List<Route>> response;
        try {
         response = restTemplate.exchange(
                ROUTES_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Route>>() {
                }
        ); } catch (RestClientException e) {
            return Collections.emptyList();
        }
        return Optional.ofNullable(response).map(ResponseEntity::getBody).orElse(Collections.emptyList());
    }

}
