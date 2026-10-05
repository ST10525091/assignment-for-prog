import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AirportSystem system = new AirportSystem();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n==========================================");
            System.out.println("  AEROLINK AIRPORT MANAGEMENT SYSTEM");
            System.out.println("==========================================");
            System.out.println("1. Register New Flight");
            System.out.println("2. Search Flight by ID");
            System.out.println("3. Update Flight Details");
            System.out.println("4. Delete Flight");
            System.out.println("5. Display All Registered Flights");
            System.out.println("6. Allocate Gate and Time Slot");
            System.out.println("7. Release Gate Allocation");
            System.out.println("8. Display Complete Gate Schedule");
            System.out.println("9. Generate Airport Operations Report");
            System.out.println("10. Sort Flights by ID");
            System.out.println("11. Sort Flights by Departure Time");
            System.out.println("12. Exit");
            System.out.print("Select an option (1-12): ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid entry. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter Flight ID: ");
                    String flightId = scanner.nextLine().trim();

                    System.out.print("Enter Airline Name: ");
                    String airline = scanner.nextLine().trim();

                    System.out.print("Enter Destination: ");
                    String destination = scanner.nextLine().trim();

                    System.out.print("Enter Departure Time (e.g. 06:00, 08:00): ");
                    String depTime = scanner.nextLine().trim();

                    System.out.print("Enter Passenger Capacity: ");
                    int capacity = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter Booked Passengers: ");
                    int booked = Integer.parseInt(scanner.nextLine());

                    System.out.println("Select Category (1: DOMESTIC, 2: INTERNATIONAL, 3: CARGO): ");
                    int catChoice = Integer.parseInt(scanner.nextLine());
                    FlightCategory category = FlightCategory.DOMESTIC;
                    if (catChoice == 2) category = FlightCategory.INTERNATIONAL;
                    else if (catChoice == 3) category = FlightCategory.CARGO;

                    System.out.println("Select Status (1: SCHEDULED, 2: BOARDING, 3: DELAYED, 4: DEPARTED, 5: CANCELLED): ");
                    int statusChoice = Integer.parseInt(scanner.nextLine());
                    FlightStatus status = FlightStatus.SCHEDULED;
                    if (statusChoice == 2) status = FlightStatus.BOARDING;
                    else if (statusChoice == 3) status = FlightStatus.DELAYED;
                    else if (statusChoice == 4) status = FlightStatus.DEPARTED;
                    else if (statusChoice == 5) status = FlightStatus.CANCELLED;

                    if (category == FlightCategory.INTERNATIONAL) {
                        System.out.print("Enter Departure Terminal: ");
                        String terminal = scanner.nextLine().trim();

                        System.out.print("Enter Boarding Gate: ");
                        String gate = scanner.nextLine().trim();

                        System.out.print("Customs Clearance Required? (true/false): ");
                        boolean customs = Boolean.parseBoolean(scanner.nextLine());

                        InternationalFlight intFlight = new InternationalFlight(flightId, airline, destination, depTime,
                                capacity, booked, category, status, terminal, gate, customs);
                        if (system.registerFlight(intFlight)) {
                            System.out.println("International flight registered successfully.");
                        }
                    } else {
                        Flight regFlight = new Flight(flightId, airline, destination, depTime, capacity, booked, category, status);
                        if (system.registerFlight(regFlight)) {
                            System.out.println("Flight registered successfully.");
                        }
                    }
                    break;

                case 2:
                    System.out.print("Enter Flight ID to search: ");
                    String searchId = scanner.nextLine().trim();
                    Flight f = system.searchFlight(searchId);
                    if (f != null) {
                        System.out.println("\n--- Flight Details ---");
                        f.displayDetails();
                    } else {
                        System.out.println("Flight not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter Flight ID to update: ");
                    String updateId = scanner.nextLine().trim();
                    System.out.print("Enter New Departure Time: ");
                    String newTime = scanner.nextLine().trim();
                    System.out.print("Enter New Booked Passenger Count: ");
                    int newBooked = Integer.parseInt(scanner.nextLine());

                    System.out.println("Select New Status (1: SCHEDULED, 2: BOARDING, 3: DELAYED, 4: DEPARTED, 5: CANCELLED): ");
                    int newStatusChoice = Integer.parseInt(scanner.nextLine());
                    FlightStatus newStatus = FlightStatus.SCHEDULED;
                    if (newStatusChoice == 2) newStatus = FlightStatus.BOARDING;
                    else if (newStatusChoice == 3) newStatus = FlightStatus.DELAYED;
                    else if (newStatusChoice == 4) newStatus = FlightStatus.DEPARTED;
                    else if (newStatusChoice == 5) newStatus = FlightStatus.CANCELLED;

                    if (system.updateFlight(updateId, newTime, newBooked, newStatus)) {
                        System.out.println("Flight updated successfully.");
                    } else {
                        System.out.println("Flight not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter Flight ID to delete: ");
                    String deleteId = scanner.nextLine().trim();
                    if (system.deleteFlight(deleteId)) {
                        System.out.println("Flight deleted successfully.");
                    } else {
                        System.out.println("Flight not found.");
                    }
                    break;

                case 5:
                    System.out.println("\n--- Registered Flights ---");
                    system.displayAllFlights();
                    break;

                case 6:
                    System.out.print("Enter Flight ID to allocate: ");
                    String allocId = scanner.nextLine().trim();
                    System.out.print("Enter Gate Index (0: G01, 1: G02, 2: G03, 3: G04, 4: G05): ");
                    int gIdx = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Time Slot Index (0: 06:00, 1: 08:00, 2: 10:00, 3: 12:00, 4: 14:00, 5: 16:00, 6: 18:00, 7: 20:00): ");
                    int tIdx = Integer.parseInt(scanner.nextLine());

                    if (system.allocateGate(allocId, gIdx, tIdx)) {
                        System.out.println("Gate allocated successfully.");
                    }
                    break;

                case 7:
                    System.out.print("Enter Gate Index to release (0-4): ");
                    int relGIdx = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Time Slot Index to release (0-7): ");
                    int relTIdx = Integer.parseInt(scanner.nextLine());

                    if (system.releaseGate(relGIdx, relTIdx)) {
                        System.out.println("Gate slot released.");
                    } else {
                        System.out.println("Invalid gate slot selection.");
                    }
                    break;

                case 8:
                    System.out.println("\n--- Airport Gate Schedule ---");
                    system.displayGateSchedule();
                    break;

                case 9:
                    system.generateOperationalReport();
                    break;

                case 10:
                    system.sortFlightsById();
                    System.out.println("Flights sorted by Flight ID.");
                    break;

                case 11:
                    system.sortFlightsByDepartureTime();
                    System.out.println("Flights sorted by Departure Time.");
                    break;

                case 12:
                    running = false;
                    System.out.println("Exiting System.");
                    break;

                default:
                    System.out.println("Invalid option. Please choose between 1 and 12.");
            }
        }
        scanner.close();
    }
}