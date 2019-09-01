package krol.flights.routes;

import krol.flights.FlightConnection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class RoutesServiceTest {

    @InjectMocks
    private RoutesService testSubject;

    @Mock
    private RoutesClient routesClient;

    private final List<Route> routeListDummy = Arrays.asList(
            new Route("AAL", "STN", null, "RYANAIR"),
            new Route("AAL", "AAR", null, "AIR"),
            new Route("AAL", "DUB", "WRO", "RYANAIR"),
            new Route("AAL", "DUB", "ALC", "RYAN"),
            new Route("AAL", "GDN", null, "RYANAIR"),
            new Route("AAR", "DUB", null, "RYANAIR"),
            new Route("AAR", "GDN", null, "RYANAIR"),
            new Route("GDN", "DUB", null, "RYANAIR")
    );

    @Test
    public void getPossibleConnectionsTest() {

        // given
        Mockito.when(routesClient.fetchRoutes()).thenReturn(routeListDummy);
        String departureAirport = "AAL";
        String arrivalAirport = "DUB";
        Set<FlightConnection> expectedResult = new HashSet<>();
        expectedResult.add(new FlightConnection("AAL", "GDN", "DUB"));

        // when
        Set<FlightConnection> result = testSubject.getPossibleConnections(departureAirport, arrivalAirport);

        //then
        Assert.assertEquals(expectedResult, result);
    }

}
