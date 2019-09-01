package krol.flights.inteconnections;

import krol.flights.routes.RoutesService;
import krol.flights.schedules.SchedulesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InterconnectionsService {

    private SchedulesClient schedulesClient;
    private RoutesService routesService;

    @Autowired
    public InterconnectionsService(SchedulesClient schedulesClient, RoutesService routesService) {
        this.schedulesClient = schedulesClient;
        this.routesService = routesService;
    }

    public List<InterconnectionFlights> findInterconnections(final String from,
                                                             final String to,
                                                             final LocalDateTime departureDateTime,
                                                             final LocalDateTime arrivalDateTime
    ) {
        Set<ConnectionAirports> connectionAirports = routesService.getPossibleConnections(from, to);
        //toDo
        return null;
    }

    private List<InterconnectionFlights> airportsToFlights(final ConnectionAirports airports, final LocalDateTime departureDateTime,
                                                           final LocalDateTime arrivalDateTime) {
        int stops = airports.getTransfer() == null ? 0 : 1;
        return stops == 0 ? getDirectConnections(airports, departureDateTime, arrivalDateTime)
                : getIndirectConnections(airports, departureDateTime, arrivalDateTime);
    }

    private List<InterconnectionFlights> getDirectConnections(final ConnectionAirports airports, final LocalDateTime departureDateTime,
                                                              final LocalDateTime arrivalDateTime) {
        final int stops = 0;
        return schedulesClient.fetchSchedules(airports.getDeparture(), airports.getArrival(),
                departureDateTime.getYear(), departureDateTime.getMonth())
                .getDays().stream()
                .filter(daySchedule -> departureDateTime.getDayOfMonth() == daySchedule.getDay())
                .flatMap(it -> it.getFlights().stream())
                .filter(flight -> flight.departureNotBefore(departureDateTime.toLocalTime()))
                .filter(flight -> flight.arriveNotAfter(arrivalDateTime.toLocalTime()))
                .map(flightSchedule -> new Leg(airports.getDeparture(), airports.getArrival(),
                        LocalDateTime.of(departureDateTime.toLocalDate(), flightSchedule.getDepartureTime()),
                        LocalDateTime.of(arrivalDateTime.toLocalDate(), flightSchedule.getArrivalTime())
                ))
                .map(leg -> new InterconnectionFlights(stops, Collections.singletonList(leg)))
                .collect(Collectors.toList());
    }

    private List<InterconnectionFlights> getIndirectConnections(final ConnectionAirports airports, final LocalDateTime departureDateTime,
                                                                final LocalDateTime arrivalDateTime) {
        final int stops = 1;
        //toDo
        return null;
    }

}
