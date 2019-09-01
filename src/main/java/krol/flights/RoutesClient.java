package krol.flights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class RoutesClient {

    private static final String ROUTES_URL = "https://services-api.ryanair.com/locate/3/routes";
    private RestTemplate restTemplate;

    @Autowired
    public RoutesClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Route> fetchRoutes() {
        ResponseEntity<List<Route>> response = restTemplate.exchange(
                ROUTES_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Route>>() {}
        );
        return response.getBody();
    }

}
