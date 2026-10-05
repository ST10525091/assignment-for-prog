import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AirportSystemTest {
    private AirportSystem system;

    @BeforeEach
    public void setUp() {
        system = new AirportSystem();
    }

    @Test
    public void testRegisterAndSearchFlight() {
        Flight flight = new Flight("FL101", "FlySafair", "Cape Town", "06:00", 180, 150, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        assertTrue(system.registerFlight(flight));
        assertNotNull(system.searchFlight("FL101"));
    }

    @Test
    public void testPreventDuplicateFlightId() {
        Flight f1 = new Flight("FL101", "FlySafair", "Cape Town", "06:00", 180, 150, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        Flight f2 = new Flight("FL101", "Airlink", "Durban", "08:00", 100, 80, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        system.registerFlight(f1);
        assertFalse(system.registerFlight(f2));
    }

    @Test
    public void testUpdateAndDeleteFlight() {
        Flight flight = new Flight("FL101", "FlySafair", "Cape Town", "06:00", 180, 150, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        system.registerFlight(flight);
        
        assertTrue(system.updateFlight("FL101", "07:00", 160, FlightStatus.DELAYED));
        assertEquals("07:00", system.searchFlight("FL101").getDepartureTime());

        assertTrue(system.deleteFlight("FL101"));
        assertNull(system.searchFlight("FL101"));
    }

    @Test
    public void testAllocateAndReleaseGate() {
        Flight flight = new Flight("FL101", "FlySafair", "Cape Town", "06:00", 180, 150, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        system.registerFlight(flight);

        assertTrue(system.allocateGate("FL101", 0, 0));
        assertTrue(system.releaseGate(0, 0));
    }

    @Test
    public void testPreventOccupiedGateAllocation() {
        Flight f1 = new Flight("FL101", "FlySafair", "Cape Town", "06:00", 180, 150, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        Flight f2 = new Flight("FL102", "Airlink", "Durban", "06:00", 100, 80, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        system.registerFlight(f1);
        system.registerFlight(f2);

        system.allocateGate("FL101", 0, 0);
        assertFalse(system.allocateGate("FL102", 0, 0)); // Gate slot is occupied
    }

    @Test
    public void testSortFlights() {
        Flight f1 = new Flight("FL200", "FlySafair", "Cape Town", "10:00", 180, 150, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        Flight f2 = new Flight("FL100", "Airlink", "Durban", "06:00", 100, 80, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        system.registerFlight(f1);
        system.registerFlight(f2);

        system.sortFlightsById();
        assertEquals("FL100", system.getFlights().get(0).getFlightId());

        system.sortFlightsByDepartureTime();
        assertEquals("06:00", system.getFlights().get(0).getDepartureTime());
    }
}