// EventScheduler.java
import java.sql.SQLOutput;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeParseException;

public class EventScheduler {
    private List<Event> events;
    private Scanner scanner;
    
    public EventScheduler() {
        // Step 7: Initialize the events list and scanner
        this.events = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }
    
    public void run() {
        boolean running = true;
        while (running) {
            // Step 8: Display menu options
            System.out.println("\n=== Event Scheduler ===");
            System.out.println("1. Add Event");
            System.out.println("2. Display All Events");
            System.out.println("3. Show Time Until Event");
            System.out.println("4. Convert Event Time to Different Timezone");
            System.out.println("5. Find Upcoming Events");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            try{
                int choice = Integer.parseInt(scanner.nextLine());

                // Step 9: Implement menu choices using switch-case
                switch (choice) {
                    case 1:
                        // Call method to add event
                        addEvent();
                        break;
                    case 2:
                        // Call method to display all events
                        displayAllEvents();
                        break;
                    case 3:
                        // Call method to show time until event
                        showTimeUntilEvent();
                        break;
                    case 4:
                        // Call method to convert event time
                        convertEventTime();
                        break;
                    case 5:
                        // Call method to find upcoming events
                        findUpcomingEvents();
                        break;
                    case 6:
                        running = false;
                        System.out.println("Exiting the Event Scheduler. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }catch(NumberFormatException e){
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void addEvent() {
        // Step 10: Implement method to get event details from user and create a new Event
        // Hint: Get name, date, time, duration, and timezone from user
        // Parse the date and time strings into LocalDateTime
        // Get a ZoneId from the timezone string
        // Create a ZonedDateTime from LocalDateTime and ZoneId
        // Create a Duration object from hours and minutes
        // Create a new Event object and add it to the events list
        try{
            System.out.println("Enter event name");
            String name = scanner.nextLine();

            System.out.println("Enter date (yyyy-MM-dd)");
            String dateStr = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateStr);

            System.out.println("Enter time (HH:mm)");
            String timeStr = scanner.nextLine();
            LocalTime time = LocalTime.parse(timeStr);

            LocalDateTime dateTime = LocalDateTime.of(date, time);

            System.out.print("Enter timezone (e.g., America/New_York, Europe/London): ");
            String timeZone = scanner.nextLine();
            ZoneId zoneId = ZoneId.of(timeZone);

            ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, zoneId);

            System.out.println("Enter duration in hours");
            int hours = Integer.parseInt(scanner.nextLine());

            System.out.println("Enter duration in minutes");
            int minutes = Integer.parseInt(scanner.nextLine());

            Duration duration = Duration.ofHours(hours).plusMinutes(minutes);

            Event event = new Event(name, zonedDateTime, duration);
            events.add(event);

            System.out.println("Event added sucessfully !");
        }catch(DateTimeParseException dte){
            System.out.println("Error: Invalid date and time format. please try again !");
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    private void displayAllEvents() {
        // Step 11: Implement method to display all events
        // Hint: Get format pattern from user
        // Loop through events list and display each event with the specified format
        if(events.isEmpty()){
            System.out.println("No events to display.");
            return;
        }

        System.out.print("Enter date format pattern (e.g., yyyy-MM-dd HH:mm z, dd-MMM-yyyy hh:mm a): ");
        String pattern = scanner.nextLine();

        System.out.println("\nAll events:");
        for(int i=0; i<events.size(); i++){
            Event event = events.get(i);
            System.out.println((i+1)+". "+event.getName()+" - "+event.formatDateTime(pattern)+
                                " (Duration: "+event.getDuration().toHours()+ " h"
                                 +event.getDuration().toMinutesPart()+" m)");
        }
    }
    
    private void showTimeUntilEvent() {
        // Step 12: Implement method to calculate and display time until a specific event
        // Hint: Show list of events with numbers
        // Get event selection from user
        // Calculate and display time until the selected event
        if(events.isEmpty()){
            System.out.println("No events available.");
            return;
        }

        System.out.println("\nSelect an event.");
        for(int i=0; i<events.size(); i++){
            System.out.println((i+1)+". "+events.get(i).getName());
        }

        try{
            System.out.println("Enter event number.");
            int eventNumber = Integer.parseInt(scanner.nextLine()) - 1;

            if(eventNumber >= 0 && eventNumber < events.size()){
                Event event = events.get(eventNumber);
                Duration timeUntil = event.timeUntilEvent();
                if(timeUntil.equals(Duration.ZERO)){
                    System.out.println("This event has already occurred.");
                }else{
                    long days = timeUntil.toDays();
                    long hours = timeUntil.toHoursPart();
                    long minutes = timeUntil.toMinutesPart();
                    long seconds = timeUntil.toSecondsPart();

                    System.out.println("Time until "+event.getName()+": "+days+" days "+hours+" hours "+minutes
                                       +" minutes "+seconds+" seconds ");
                }
            }else{
                System.out.println("Invalid event number.");
            }
        }catch(NumberFormatException e){
            System.out.println("Please enter a valid number.");
        }
    }
    
    private void convertEventTime() {
        // Step 13: Implement method to convert event time to a different timezone
        // Hint: Show list of events with numbers
        // Get event selection from user
        // Get target timezone from user
        // Convert and display event time in the target timezone
        if(events.isEmpty()){
            System.out.println("No events available.");
            return;
        }

        System.out.println("\nSelect an event:");
        for(int i=0; i<events.size(); i++){
            System.out.println((i+1)+". "+events.get(i).getName());
        }

        try{
            System.out.println("Enter event number.");
            int eventNumber = Integer.parseInt(scanner.nextLine()) - 1;

            if(eventNumber >=0 && eventNumber < events.size()){
                Event event = events.get(eventNumber);

                System.out.print("Enter target timezone (e.g., America/New_York, Europe/London): ");
                String targetTimezone = scanner.nextLine();

                try{
                    ZonedDateTime convertedTime = event.convertToTimezone(targetTimezone);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");
                    System.out.println("Event in "+targetTimezone+": "+convertedTime.format(formatter));
                }catch(Exception e){
                    System.out.println("Invalid timezone. Please try again.");
                }
            }
        }catch(NumberFormatException e){
            System.out.println("Please enter a valid number.");
        }
    }
    
    private void findUpcomingEvents() {
        // Step 14: Implement method to find events within a specific number of days
        // Hint: Get number of days from user
        // Loop through events and display those within the specified days
        if(events.isEmpty()){
            System.out.println("No events available.");
            return;
        }

        try{
            System.out.println("Enter number of days to look ahead.");
            int days = Integer.parseInt(scanner.nextLine());

            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime endDate = ZonedDateTime.now().plusDays(days);

            System.out.println("Events is next "+days+" days");
            boolean found = false;

            for(Event event : events){
                ZonedDateTime eventTime = event.getDateTime();
                if(eventTime.isAfter(now) && eventTime.isBefore(endDate)){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");
                    System.out.println("- "+event.getName()+" - "+eventTime.format(formatter));
                    found = true;
                }
            }

            if(!found){
                System.out.println("No events found in the next "+days+" days.");
            }

        }catch(NumberFormatException e){
            System.out.println("Please enter a valid number.");
        }
    }
    
    public static void main(String[] args) {
        // Step 15: Create an EventScheduler object and call its run method
        EventScheduler scheduler = new EventScheduler();
        scheduler.run();
    }
}
