package krol.flights;

import krol.flights.inteconnections.InterconnectionFlights;
import krol.flights.inteconnections.InterconnectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ApiController {

    @Autowired
    private InterconnectionsService interconnectionsService;

    @GetMapping("interconnections")
    public ResponseEntity getInterconnections(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDateTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrivalDateTime
    ) {
        if(departureDateTime.isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Departure time should be later than now");
        }
        List<InterconnectionFlights> response = interconnectionsService.findInterconnections(departure, arrival, departureDateTime, arrivalDateTime);
        return ResponseEntity.ok(response);
    }

}
