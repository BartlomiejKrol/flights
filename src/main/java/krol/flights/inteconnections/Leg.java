package krol.flights.inteconnections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Leg {
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
}
