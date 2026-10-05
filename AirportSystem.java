import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AirportSystem {
    private List<Flight> flights;
    private String[][] gateSchedule; // 5 gates x 8 departure time slots
    private String[] timeSlots = {"06:00", "08:00", "10:00", "12:00", "14:00", "16:00", "18:00", "20:00"};
    private String[] gates = {"Gate G01", "Gate G02", "Gate G03", "Gate G04", "Gate G05"};

    public AirportSystem() {
        flights = new ArrayList<>();
        gateSchedule = new String[5][8];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                gateSchedule[i][j] = "—";
            }
        }
    }

    // Feature 1: Flight Management
    public boolean registerFlight(Flight flight) {
        for (Flight f : flights) {
            if (f.getFlightId().equalsIgnoreCase(flight.getFlightId())) {
                System.out.println("Error: Duplicate Flight ID detected.");
                return false;
            }
        }
        flights.add(flight);
        return true;
    }

    public Flight searchFlight(String flightId) {
        for (Flight f : flights) {
            if (f.getFlightId().equalsIgnoreCase(flightId)) {
                return f;
            }
        }
        return null;
    }

    public boolean updateFlight(String flightId, String newTime, int newBooked, FlightStatus newStatus) {
        Flight f = searchFlight(flightId);
        if (f != null) {
            f.setDepartureTime(newTime);
            f.setBookedPassengers(newBooked);
            f.setStatus(newStatus);

            // Automatically release gate slot if status changes to CANCELLED
            if (newStatus == FlightStatus.CANCELLED) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (gateSchedule[i][j].equalsIgnoreCase(flightId)) {
                            gateSchedule[i][j] = "—";
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean deleteFlight(String flightId) {
        Flight f = searchFlight(flightId);
        if (f != null) {
            flights.remove(f);
            // Remove allocation from schedule grid
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 8; j++) {
                    if (gateSchedule[i][j].equalsIgnoreCase(flightId)) {
                        gateSchedule[i][j] = "—";
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void displayAllFlights() {
        if (flights.isEmpty()) {
            System.out.println("No registered flights available.");
            return;
        }
        for (Flight f : flights) {
            f.displayDetails();
            System.out.println("----------------------------------");
        }
    }

    // Feature 2: Gate Scheduling Management
    public boolean allocateGate(String flightId, int gateIndex, int slotIndex) {
        if (gateIndex < 0 || gateIndex >= 5 || slotIndex < 0 || slotIndex >= 8) {
            System.out.println("Error: Invalid gate or time slot selection.");
            return false;
        }

        // Prevent duplicate scheduling for the same flight
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                if (gateSchedule[i][j].equalsIgnoreCase(flightId)) {
                    System.out.println("Error: Flight is already assigned to a gate slot.");
                    return false;
                }
            }
        }

        // Prevent double booking on occupied gate/slot
        if (!gateSchedule[gateIndex][slotIndex].equals("—")) {
            System.out.println("Error: Gate slot is already occupied.");
            return false;
        }

        gateSchedule[gateIndex][slotIndex] = flightId;
        return true;
    }

    public boolean releaseGate(int gateIndex, int slotIndex) {
        if (gateIndex >= 0 && gateIndex < 5 && slotIndex >= 0 && slotIndex < 8) {
            gateSchedule[gateIndex][slotIndex] = "—";
            return true;
        }
        return false;
    }

    public void displayGateSchedule() {
        System.out.print(String.format("%-10s", "Gate"));
        for (String slot : timeSlots) {
            System.out.print(String.format("%-10s", slot));
        }
        System.out.println();

        // Display matrix using nested loops
        for (int i = 0; i < 5; i++) {
            System.out.print(String.format("%-10s", gates[i]));
            for (int j = 0; j < 8; j++) {
                System.out.print(String.format("%-10s", gateSchedule[i][j]));
            }
            System.out.println();
        }
    }

    // Feature 3: Airport Operational Reports
    public void generateOperationalReport() {
        int scheduledCount = 0;
        int delayedCount = 0;
        double totalLoadPct = 0;

        for (Flight f : flights) {
            if (f.getStatus() == FlightStatus.SCHEDULED) scheduledCount++;
            if (f.getStatus() == FlightStatus.DELAYED) delayedCount++;
            totalLoadPct += f.getPassengerLoadPercentage();
        }

        // Determine busiest gate
        int busiestGateIdx = -1;
        int maxGateFlights = -1;
        for (int i = 0; i < 5; i++) {
            int count = 0;
            for (int j = 0; j < 8; j++) {
                if (!gateSchedule[i][j].equals("—")) count++;
            }
            if (count > maxGateFlights) {
                maxGateFlights = count;
                busiestGateIdx = i;
            }
        }

        // Determine busiest departure time slot
        int busiestSlotIdx = -1;
        int maxSlotFlights = -1;
        for (int j = 0; j < 8; j++) {
            int count = 0;
            for (int i = 0; i < 5; i++) {
                if (!gateSchedule[i][j].equals("—")) count++;
            }
            if (count > maxSlotFlights) {
                maxSlotFlights = count;
                busiestSlotIdx = j;
            }
        }

        // Highest passenger load
        Flight highestLoadFlight = null;
        double maxLoad = -1;
        for (Flight f : flights) {
            if (f.getPassengerLoadPercentage() > maxLoad) {
                maxLoad = f.getPassengerLoadPercentage();
                highestLoadFlight = f;
            }
        }

        System.out.println("------------------------------------------");
        System.out.println("     AEROLINK AIRPORT OPERATIONS REPORT");
        System.out.println("------------------------------------------");
        System.out.println("Scheduled Flights: " + scheduledCount);
        System.out.println("Delayed Flights:   " + delayedCount);
        System.out.println();
        System.out.println("Busiest Gate:");
        if (busiestGateIdx != -1) {
            System.out.println(gates[busiestGateIdx] + " - " + maxGateFlights + " Scheduled Flights");
        }
        System.out.println();
        System.out.println("Busiest Departure Time:");
        if (busiestSlotIdx != -1) {
            System.out.println(timeSlots[busiestSlotIdx] + " - " + maxSlotFlights + " Scheduled Departures");
        }
        System.out.println();
        System.out.println("Highest Passenger Load:");
        if (highestLoadFlight != null) {
            System.out.println("Flight ID: " + highestLoadFlight.getFlightId());
            System.out.println("Booked Passengers: " + highestLoadFlight.getBookedPassengers());
            System.out.println("Passenger Capacity: " + highestLoadFlight.getPassengerCapacity());
            System.out.println(String.format("Passenger Load: %.2f%%", highestLoadFlight.getPassengerLoadPercentage()));
        }
        System.out.println();
        double avgLoad = flights.isEmpty() ? 0 : totalLoadPct / flights.size();
        System.out.println(String.format("Average Passenger Load: %.2f%%", avgLoad));
        System.out.println("==========================================");
    }

    public void sortFlightsById() {
        Collections.sort(flights, Comparator.comparing(Flight::getFlightId));
    }

    public void sortFlightsByDepartureTime() {
        Collections.sort(flights, Comparator.comparing(Flight::getDepartureTime));
    }

    public List<Flight> getFlights() { return flights; }
}