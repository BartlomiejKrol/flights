package krol.flights;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.Set;

@Service
public class TimeService {

    public Set<YearMonth> getYearMonthsBetween(LocalDateTime start, LocalDateTime end) {
        YearMonth startYearMonth = YearMonth.of(start.getYear(), start.getMonth());
        YearMonth endYearMonth = YearMonth.of(end.getYear(), end.getMonth());
        Set<YearMonth> resultSet = new HashSet<>();
        YearMonth iterator = startYearMonth;
        while (endYearMonth.isAfter(iterator)) {
            resultSet.add(iterator);
            iterator = iterator.plusMonths(1);
        }
        return resultSet;
    }

}
