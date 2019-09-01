package krol.flights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoutesService {

    private RoutesClient routesClient;

    @Autowired
    public RoutesService(RoutesClient routesClient) {
        this.routesClient = routesClient;
    }

    public Set<Connection> getPossibleConnections(String departureAirport, String arrivalAirport) {
        Set<Route> allUsableRoutes = routesClient.fetchRoutes()
                .stream()
                .filter(Route::couldBeUsed)
                .collect(Collectors.toSet());
        Optional<Connection> directConnection = getDirectConnection(departureAirport, arrivalAirport, allUsableRoutes);
        Set<Connection> connections = getIndirectConnections(departureAirport, arrivalAirport, allUsableRoutes);
        directConnection.ifPresent(connections::add);

        return connections;
    }

    private Optional<Connection> getDirectConnection(String departureAirport, String arrivalAirport, Set<Route> routes) {
        if (routes.stream().anyMatch(route -> route.isFrom(departureAirport) && route.isTo(arrivalAirport))) {
            return Optional.of(new Connection(departureAirport, arrivalAirport));
        } else {
            return Optional.empty();
        }
    }


    private Set<Connection> getIndirectConnections(String departure, String arrival, Set<Route> routes) {
        final Set<Route> routesToArrival = routes.stream()
                .filter(route -> route.isTo(arrival))
                .collect(Collectors.toSet());
        return routes.stream()
                .filter(route -> route.isFrom(departure))
                .filter(routeFrom ->
                        routesToArrival
                                .stream()
                                .anyMatch(routeTo -> routeTo.isFrom(routeFrom.getAirportTo()))
                )
                .map(route ->
                        new Connection(route.getAirportFrom(), route.getAirportTo(), arrival)
                )
                .collect(Collectors.toSet());
    }

}
