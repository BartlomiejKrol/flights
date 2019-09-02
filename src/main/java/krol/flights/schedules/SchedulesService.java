package krol.flights.schedules;

import javafx.util.Pair;
import krol.flights.TimeService;
import krol.flights.inteconnections.Leg;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SchedulesService {

    private TimeService timeService;
    private SchedulesClient schedulesClient;

    public Set<YearMonthSchedule> getSchedule(final String departure, final String arrival, final LocalDateTime departureDateTime,
                                              final LocalDateTime arrivalDateTime) {
        return timeService.getYearMonthsBetween(departureDateTime, arrivalDateTime).stream()
                .map(yearMonth -> new YearMonthSchedule(yearMonth,
                        schedulesClient.fetchSchedules(departure, arrival, yearMonth.getYear(), yearMonth.getMonth()).getDays()))
                .collect(Collectors.toSet());
    }

    public Set<Leg> getLegsSet(final String departure, final String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        Set<YearMonthSchedule> schedule = getSchedule(departure, arrival, departureDateTime, arrivalDateTime);
        return schedule.stream()
                .flatMap(yearMonthSchedule -> yearMonthSchedule.getDays().stream()
                        .map(daySchedule -> new Pair<LocalDate, List<FlightSchedule>>(LocalDate.of(yearMonthSchedule.getYearMonth().getYear(),
                                yearMonthSchedule.getYearMonth().getMonth(),
                                daySchedule.getDay()), daySchedule.getFlights())))
                .filter(dayPair -> departureDateTime.getDayOfMonth() == dayPair.getKey().getDayOfMonth())
                .flatMap(dayPair -> dayPair.getValue().stream()
                        .map(flight -> new Pair<LocalDate, FlightSchedule>(dayPair.getKey(), flight))
                )
                .filter(flightPair -> flightPair.getValue().departureNotBefore(departureDateTime.toLocalTime()))
                .filter(flightPair -> flightPair.getValue().arriveNotAfter(arrivalDateTime.toLocalTime()))
                .map(flightPair -> new Leg(departure, arrival,
                        LocalDateTime.of(flightPair.getKey(), flightPair.getValue().getDepartureTime()),
                        LocalDateTime.of(flightPair.getKey(), flightPair.getValue().getArrivalTime())
                ))
                .collect(Collectors.toSet());
    }
}
