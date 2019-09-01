package krol.flights.inteconnections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterconnectionFlights {
    private @Range(min = 0, max = 1) int stops;
    private List<Leg> legs;
}
