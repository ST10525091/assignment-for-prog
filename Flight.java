public class Flight {
    private String flightId;
    private String airlineName;
    private String destination;
    private String departureTime;
    private int passengerCapacity;
    private int bookedPassengers;
    private FlightCategory category;
    private FlightStatus status;

    public Flight(String flightId, String airlineName, String destination, String departureTime, 
                  int passengerCapacity, int bookedPassengers, FlightCategory category, FlightStatus status) {
        this.flightId = flightId;
        this.airlineName = airlineName;
        this.destination = destination;
        this.departureTime = departureTime;
        this.passengerCapacity = passengerCapacity;
        setBookedPassengers(bookedPassengers);
        this.category = category;
        this.status = status;
    }

    // Getters and Setters
    public String getFlightId() { return flightId; }
    public void setFlightId(String flightId) { this.flightId = flightId; }

    public String getAirlineName() { return airlineName; }
    public void setAirlineName(String airlineName) { this.airlineName = airlineName; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public int getPassengerCapacity() { return passengerCapacity; }
    public void setPassengerCapacity(int passengerCapacity) { this.passengerCapacity = passengerCapacity; }

    public int getBookedPassengers() { return bookedPassengers; }
    public void setBookedPassengers(int bookedPassengers) {
        if (bookedPassengers <= this.passengerCapacity) {
            this.bookedPassengers = bookedPassengers;
        } else {
            System.out.println("Error: Booked passengers cannot exceed passenger capacity. Setting to maximum capacity.");
            this.bookedPassengers = this.passengerCapacity;
        }
    }

    public FlightCategory getCategory() { return category; }
    public void setCategory(FlightCategory category) { this.category = category; }

    public FlightStatus getStatus() { return status; }
    public void setStatus(FlightStatus status) { this.status = status; }

    public double getPassengerLoadPercentage() {
        if (passengerCapacity == 0) return 0.0;
        return ((double) bookedPassengers / passengerCapacity) * 100;
    }

    public void displayDetails() {
        System.out.println("Flight ID: " + flightId);
        System.out.println("Airline: " + airlineName);
        System.out.println("Destination: " + destination);
        System.out.println("Departure Time: " + departureTime);
        System.out.println("Passenger Capacity: " + passengerCapacity);
        System.out.println("Booked Passengers: " + bookedPassengers);
        System.out.println("Category: " + category);
        System.out.println("Status: " + status);
    }
}