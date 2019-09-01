package krol.flights;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class FlightConnection {

    public FlightConnection(String departure, String arrival) {
        this.departure = departure;
        this.transfer = null;
        this.arrival = arrival;
    }

    private String departure;
    private String transfer;
    private String arrival;
}
