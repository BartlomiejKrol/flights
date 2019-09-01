package krol.flights.schedules;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Month;
import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class MonthSchedule {
    private Month month;
    private List<DaySchedule> days;
}
