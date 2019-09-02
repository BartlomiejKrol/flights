package krol.flights.inteconnections;

import krol.flights.routes.RoutesService;
import krol.flights.schedules.SchedulesService;
import lombok.extern.slf4j.Slf4j;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InterconnectionsService {

    private static final int MIN_TRANSFER_TIME_HOURS = 2;

    private RoutesService routesService;
    private SchedulesService schedulesService;

    @Autowired
    public InterconnectionsService(RoutesService routesService, SchedulesService schedulesService) {
        this.routesService = routesService;
        this.schedulesService = schedulesService;
    }

    public List<InterconnectionFlights> findInterconnections(final String from,
                                                             final String to,
                                                             final LocalDateTime departureDateTime,
                                                             final LocalDateTime arrivalDateTime
    ) {
        Set<ConnectionAirports> connectionAirports = routesService.getPossibleConnections(from, to);
        return connectionAirports.stream()
                .flatMap(airports -> airportsToFlights(airports, departureDateTime, arrivalDateTime).stream())
                .collect(Collectors.toList());
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
        return schedulesService
                .getLegsSet(airports.getDeparture(), airports.getDeparture(), departureDateTime, arrivalDateTime).stream()
                .map(leg -> new InterconnectionFlights(stops, Collections.singletonList(leg)))
                .collect(Collectors.toList());
    }

    private List<InterconnectionFlights> getIndirectConnections(final ConnectionAirports airports, final LocalDateTime departureDateTime,
                                                                final LocalDateTime arrivalDateTime) {
        final int stops = 1;

        LocalDateTime latestTransferArrival = arrivalDateTime.minusHours(MIN_TRANSFER_TIME_HOURS);

        final Set<Leg> possibleFirstLegs = schedulesService.getLegsSet(airports.getDeparture(),
                airports.getTransfer(), departureDateTime, latestTransferArrival);

        LocalDateTime earliestTransferDeparture;
        try {
            earliestTransferDeparture = possibleFirstLegs.stream()
                    .map(Leg::getArrivalDateTime)
                    .min(LocalDateTime::compareTo)
                    .orElseThrow(NotFound::new)
                    .plusHours(MIN_TRANSFER_TIME_HOURS);
        } catch (NotFound notFound) {
            log.warn("Not found earliestTransferDeparture");
            return Collections.emptyList();
        }
        final Set<Leg> possibleSecondLegs = schedulesService.getLegsSet(airports.getTransfer(), airports.getArrival(), earliestTransferDeparture, arrivalDateTime);

        return possibleFirstLegs.stream()
                .flatMap(firstLeg -> possibleSecondLegs.stream()
                        .filter(secondLeg -> secondLeg.getDepartureDateTime().isAfter(firstLeg.getArrivalDateTime().plusHours(MIN_TRANSFER_TIME_HOURS)))
                        .map(secondLeg -> Arrays.asList(firstLeg, secondLeg))
                )
                .map(legs -> new InterconnectionFlights(stops, legs))
                .collect(Collectors.toList());
    }

}
