package krol.flights.routes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {
    private static final String ALLOWED_OPERATOR = "RYANAIR";
    private String airportFrom;
    private String airportTo;
    private String connectingAirport;
    private String operator;

    public boolean couldBeUsed() {
        return this.connectingAirport == null && this.operator.equals(ALLOWED_OPERATOR);
    }

    public boolean isFrom(String departureAirport) {
        return this.airportFrom.equals(departureAirport);
    }

    public boolean isTo(String arrivalAirport) {
        return this.airportTo.equals(arrivalAirport);
    }
}
