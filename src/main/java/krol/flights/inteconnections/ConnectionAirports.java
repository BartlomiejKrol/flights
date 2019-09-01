package krol.flights.inteconnections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ConnectionAirports {

    public ConnectionAirports(String departure, String arrival) {
        this.departure = departure;
        this.transfer = null;
        this.arrival = arrival;
    }

    private String departure;
    private String transfer;
    private String arrival;
}
