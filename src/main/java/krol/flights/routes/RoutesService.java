package krol.flights.routes;

import krol.flights.FlightConnection;
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

    public Set<FlightConnection> getPossibleConnections(String departureAirport, String arrivalAirport) {
        Set<Route> allUsableRoutes = routesClient.fetchRoutes()
                .stream()
                .filter(Route::couldBeUsed)
                .collect(Collectors.toSet());
        Optional<FlightConnection> directConnection = getDirectConnection(departureAirport, arrivalAirport, allUsableRoutes);
        Set<FlightConnection> flightConnections = getIndirectConnections(departureAirport, arrivalAirport, allUsableRoutes);
        directConnection.ifPresent(flightConnections::add);

        return flightConnections;
    }

    private Optional<FlightConnection> getDirectConnection(String departureAirport, String arrivalAirport, Set<Route> routes) {
        if (routes.stream().anyMatch(route -> route.isFrom(departureAirport) && route.isTo(arrivalAirport))) {
            return Optional.of(new FlightConnection(departureAirport, arrivalAirport));
        } else {
            return Optional.empty();
        }
    }


    private Set<FlightConnection> getIndirectConnections(String departure, String arrival, Set<Route> routes) {
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
                        new FlightConnection(route.getAirportFrom(), route.getAirportTo(), arrival)
                )
                .collect(Collectors.toSet());
    }

}
