package krol.flights.schedules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Month;
import java.util.HashMap;
import java.util.Map;

@Component
public class SchedulesClient {
    private static final String DEPARTURE = "departure";
    private static final String ARRIVAL = "arrival";
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String SCHEDULES_URL =
            "https://services-api.ryanair.com/timtbl/3/schedules/{" + DEPARTURE + "}/{" + ARRIVAL + "}/years/{" + YEAR + "}/months/{" + MONTH + "}";
    private RestTemplate restTemplate;

    @Autowired
    public SchedulesClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MonthSchedule fetchSchedules(String departure, String arrival, int year, Month month) {
        Map<String, String> params = new HashMap<>();
        params.put(DEPARTURE, departure);
        params.put(ARRIVAL, arrival);
        params.put(YEAR, String.valueOf(year));
        params.put(MONTH, String.valueOf(month.getValue()));

        ResponseEntity<MonthSchedule> response = restTemplate.exchange(
                SCHEDULES_URL,
                HttpMethod.GET,
                null,
                MonthSchedule.class,
                params
        );

        return response.getBody();
    }

}
