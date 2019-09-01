package krol.flights.schedules;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthSchedule {
    private Month month;
    private List<DaySchedule> days;

    public void setMonth(int month) {
        this.month = Month.of(month);
    }

}
