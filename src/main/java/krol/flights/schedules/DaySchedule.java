package krol.flights.schedules;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.DayOfWeek;
import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class DaySchedule {
    private DayOfWeek day;
    private List<FlightSchedule> flights;
}
