package krol.flights.schedules;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FlightSchedule {
    private String number;
    private String departureTime;
    private String arrivalTime;
}
