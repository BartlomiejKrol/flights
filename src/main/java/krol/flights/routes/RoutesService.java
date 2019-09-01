package krol.flights.routes;

import krol.flights.inteconnections.ConnectionAirports;
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

    public Set<ConnectionAirports> getPossibleConnections(String departureAirport, String arrivalAirport) {
        Set<Route> allUsableRoutes = routesClient.fetchRoutes()
                .stream()
                .filter(Route::couldBeUsed)
                .collect(Collectors.toSet());
        Optional<ConnectionAirports> directConnection = getDirectConnection(departureAirport, arrivalAirport, allUsableRoutes);
        Set<ConnectionAirports> connectionAirports = getIndirectConnections(departureAirport, arrivalAirport, allUsableRoutes);
        directConnection.ifPresent(connectionAirports::add);

        return connectionAirports;
    }

    private Optional<ConnectionAirports> getDirectConnection(String departureAirport, String arrivalAirport, Set<Route> routes) {
        if (routes.stream().anyMatch(route -> route.isFrom(departureAirport) && route.isTo(arrivalAirport))) {
            return Optional.of(new ConnectionAirports(departureAirport, arrivalAirport));
        } else {
            return Optional.empty();
        }
    }


    private Set<ConnectionAirports> getIndirectConnections(String departure, String arrival, Set<Route> routes) {
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
                        new ConnectionAirports(route.getAirportFrom(), route.getAirportTo(), arrival)
                )
                .collect(Collectors.toSet());
    }

}
