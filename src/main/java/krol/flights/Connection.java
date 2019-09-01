package krol.flights;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Connection {

    public Connection(String departure, String arrival) {
        this.departure = departure;
        this.transfer = null;
        this.arrival = arrival;
    }

    private String departure;
    private String transfer;
    private String arrival;
}
