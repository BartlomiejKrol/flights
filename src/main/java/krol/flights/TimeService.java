package krol.flights;

import krol.flights.schedules.FlightSchedule;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.Set;

@Service
@NoArgsConstructor
public class TimeService {

    public Set<YearMonth> getYearMonthsBetween(LocalDateTime start, LocalDateTime end) {
        YearMonth startYearMonth = YearMonth.of(start.getYear(), start.getMonth());
        YearMonth endYearMonth = YearMonth.of(end.getYear(), end.getMonth());
        Set<YearMonth> resultSet = new HashSet<>();
        YearMonth iterator = startYearMonth;
        while (endYearMonth.isAfter(iterator) || endYearMonth.equals(iterator)) {
            resultSet.add(iterator);
            iterator = iterator.plusMonths(1);
        }
        return resultSet;
    }

    public boolean isFlightDepartureNotBefore(LocalDate flightScheduleDate, FlightSchedule flight, LocalDateTime dateTime) {
        LocalDateTime flightDepartureDateTime = LocalDateTime.of(flightScheduleDate, flight.getDepartureTime());
        return flightDepartureDateTime.isAfter(dateTime) || flightDepartureDateTime.equals(dateTime);
    }

    public boolean isFlightArrivalNotAfter(LocalDate flightScheduleDate, FlightSchedule flight, LocalDateTime dateTime) {
        LocalDateTime flightArrivalDateTime = isArrivalOnSameDate(flight) ?
                LocalDateTime.of(flightScheduleDate, flight.getDepartureTime()) : LocalDateTime.of(flightScheduleDate, flight.getDepartureTime()).plusDays(1);
        return flightArrivalDateTime.isBefore(dateTime) || flightArrivalDateTime.equals(dateTime);
    }

    public LocalDateTime getFlightDepartureDateTime(LocalDate departureDate, FlightSchedule flight) {
        return LocalDateTime.of(departureDate, flight.getDepartureTime());
    }

    public LocalDateTime getFlightArrivalDateTime(LocalDate departureDate, FlightSchedule flight) {
        LocalDate arrivalDate = isArrivalOnSameDate(flight) ? departureDate : departureDate.plusDays(1);
        return LocalDateTime.of(arrivalDate, flight.getArrivalTime());
    }

    private boolean isArrivalOnSameDate(FlightSchedule flight) {
        return flight.getArrivalTime().isAfter(flight.getDepartureTime());
    }

}
