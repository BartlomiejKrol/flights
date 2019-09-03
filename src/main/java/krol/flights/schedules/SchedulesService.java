package krol.flights.schedules;

import javafx.util.Pair;
import krol.flights.TimeService;
import krol.flights.inteconnections.Leg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SchedulesService {

    private TimeService timeService;
    private SchedulesClient schedulesClient;

    @Autowired
    public SchedulesService(TimeService timeService, SchedulesClient schedulesClient) {
        this.timeService = timeService;
        this.schedulesClient = schedulesClient;
    }


    public Set<Leg> getLegsSet(final String departure, final String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        Set<YearMonthSchedule> schedule = getSchedule(departure, arrival, departureDateTime, arrivalDateTime);
        return schedule.stream()
                .flatMap(yearMonthSchedule -> yearMonthSchedule.getDays().stream()
                        .map(daySchedule -> new Pair<>(LocalDate.of(yearMonthSchedule.getYearMonth().getYear(),
                                yearMonthSchedule.getYearMonth().getMonth(),
                                daySchedule.getDay()), daySchedule.getFlights())))
                .flatMap(dayPair -> dayPair.getValue().stream()
                        .map(flight -> new Pair<>(dayPair.getKey(), flight))
                )
                .filter(flightPair -> timeService.isFlightDepartureNotBefore(flightPair.getKey(), flightPair.getValue(), departureDateTime))
                .filter(flightPair -> timeService.isFlightArrivalNotAfter(flightPair.getKey(), flightPair.getValue(), arrivalDateTime))
                .map(flightPair -> new Leg(
                        departure,
                        arrival,
                        timeService.getFlightDepartureDateTime(flightPair.getKey(), flightPair.getValue()),
                        timeService.getFlightArrivalDateTime(flightPair.getKey(), flightPair.getValue()))
                )
                .collect(Collectors.toSet());
    }

    private Set<YearMonthSchedule> getSchedule(final String departure, final String arrival, final LocalDateTime departureDateTime,
                                               final LocalDateTime arrivalDateTime) {

        Set<YearMonth> yearMonths = timeService.getYearMonthsBetween(departureDateTime, arrivalDateTime);
        return yearMonths.stream()
                .map(yearMonth -> new YearMonthSchedule(yearMonth,
                        schedulesClient.fetchSchedules(departure, arrival, yearMonth.getYear(), yearMonth.getMonth()).orElse(
                                new MonthSchedule(yearMonth.getMonth(), Collections.emptyList()))
                                .getDays()))
                .collect(Collectors.toSet());
    }
}
