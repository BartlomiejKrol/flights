package krol.flights.schedules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.time.YearMonth;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class YearMonthSchedule {
    private YearMonth yearMonth;
    private List<DaySchedule> days;
}
