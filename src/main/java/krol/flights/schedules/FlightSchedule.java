package krol.flights.schedules;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightSchedule {
    private LocalTime departureTime;
    private LocalTime arrivalTime;

    public boolean departureNotBefore(LocalTime departureTime) {
        return getDepartureTime().isAfter(departureTime) ||
                getDepartureTime().equals(departureTime);
    }

    public boolean arriveNotAfter(LocalTime arrivalTime) {
        return getArrivalTime().isBefore(arrivalTime) ||
                getArrivalTime().equals(arrivalTime);
    }
}
