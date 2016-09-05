// author: Mark W. Naylor
// file:   Venue.java
// date:   2016-Aug-28

package com.walmart.homework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

import java.util.concurrent.CopyOnWriteArrayList;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The Venue class implemts the TicketService interface, manages the
 * objects needed to keep track of the state of tickets/seats.
 *
 * Note: The implied requirement from the assignment is that the Level
 * Ids are (approximately) inversely proportional to the price of the
 * ticket in that level.  The static builders explicitly create the
 * Levels and the Level collections.  One of the more dynamic
 * approaches avoid hard-coded id #s, sort Levels by descending price,
 * and assign incremental ids to each.
 *
 */
public class Venue implements TicketService, AutoCloseable {
    /**
     * The collection of seating Levels available
     */
    private ArrayList<Level> levels;
    /**
     * The collection of current SeatHolds
     */
    private CopyOnWriteArrayList<SeatHold> seatHolds = new CopyOnWriteArrayList<SeatHold>();
    private Optional<Sweeper> sweeper = Optional.empty();

    /**
     * Constructs an empty Venue.
     */
    public Venue () {
        levels = new ArrayList<Level>();
        startSweeper();
    } // Venue() - constructor

    /**
     * Constructs a Venue with a the given collection of Levels
     * @param levels a Collection of Levels
     */
    public Venue (Collection<Level> levels) {
        this.levels = new ArrayList<Level>(levels);
        startSweeper();
    } // Venue() - constructor

    /**
     * Create, start, and assign the thread that releases holds that
     * have passed the expiration time.
     */
    private void startSweeper() {
        Sweeper thread = new Sweeper();
        thread.start();
        sweeper = Optional.of(thread);
    } // startSweeper

    /**
     * Builder function that creates the Venue described in the
     * homework assignment.
     * @return a default Venue
     */
    public static Venue defaultVenue() {
        Level l1 = new Level(1, new LevelName("Orchestra"), new MonetaryAmount(100.00), 25, 50);
        Level l2 = new Level(2, new LevelName("Main"), new MonetaryAmount(75.00), 20, 100);
        Level l3 = new Level(3, new LevelName("Balcony 1"), new MonetaryAmount(50.00), 15, 100);
        Level l4 = new Level(4, new LevelName("Balcony 2"), new MonetaryAmount(40.00), 15, 100);

        return new Venue(Arrays.asList(l1, l2, l3, l4));
    } // defaultVenue()

    /**
     * Builder function that creates a smaller Venue for use with the
     * JUnit tests.
     * @return a test Venue
     */
    public static Venue testingVenue() {
        Level l1 = new Level(1, new LevelName("Orchestra"), new MonetaryAmount(100.00), 1, 10);
        Level l2 = new Level(2, new LevelName("Main"), new MonetaryAmount(75.00), 1, 10);
        Level l3 = new Level(3, new LevelName("Balcony 1"), new MonetaryAmount(50.00), 1, 10);
        Level l4 = new Level(4, new LevelName("Balcony 2"), new MonetaryAmount(40.00), 1, 10);

        return new Venue(Arrays.asList(l1, l2, l3, l4));
    } // testingVenue()

    public int numSeatsAvailable(Optional<Integer> venueLevel) {
        Stream<Level> levelStream = levels.stream();
        if (venueLevel.isPresent()) {
            int result =
                levelStream.filter(level -> level.getId() == venueLevel.get())
                .map(level -> level.numSeatsAvailable())
                .findFirst()
                .orElse(0);
            return result;
        } else {
            int result = levelStream.mapToInt(level -> level.numSeatsAvailable())
                .sum();
            return result;
        }

    } // numSeatsAvailable()

    /**
     * The number of seats in a collection of Levels that are neither held nor reserved
     *
     * @param levels a collection of Levels limiting availability search/filter
     * @return the number of tickets available on the provided Levels
     */

    public int numSeatsAvailable(Collection<Level> levels) {
        Stream<Level> levelStream = levels.stream();
        return levelStream.mapToInt(level -> level.numSeatsAvailable())
            .sum();
    } // numSeatsAvailable()

    public synchronized Optional<SeatHold>
    findAndHoldSeats(int requiredSeats,
                     Optional<Integer> minLevel,
                     Optional<Integer> maxLevel,
                     String customerEmail) {

        Stream<Level> stream = levels.stream();

        // Filter out any levels below minimum desired.
        if (minLevel.isPresent()) {
            stream = stream.filter( n -> n.getId() >= minLevel.get() );
        }

        // Filter out any levels above the maximum desired.
        if (maxLevel.isPresent()) {
            stream = stream.filter( n -> n.getId() <= maxLevel.get() );
        }

        ArrayList<Level> desiredLevels = new ArrayList<Level>(stream.collect(Collectors.toList()));
        int seatCount = numSeatsAvailable(desiredLevels);

        // EARLY EXIT.  Not enoughs seats available to meet the request, send bark empty object.
        if (requiredSeats > seatCount) { return Optional.empty(); }

        SeatHold seatHold = new SeatHold(customerEmail, Optional.empty());

        for (Iterator<Level> levels = desiredLevels.iterator(); requiredSeats > 0 && levels.hasNext(); ) {
            Level level = levels.next();
            Collection<Seat> seats = level.holdNumberOfSeats(requiredSeats);
            seatHold.add(seats);
            requiredSeats -= seats.size();
        }

        seatHolds.add(seatHold);

        return Optional.of(seatHold);
    } // findAndHoldSeats()

    public int holdCount() {
        return seatHolds.size();
    } // holdCount()

    public String reserveSeats(int seatHoldId, String customerEmail) {
        String code = "No Reservation.";

        Optional<SeatHold> seatHold = seatHolds.stream()
            .filter(hold -> hold.getId() == seatHoldId)
            .findFirst();

        if (seatHold.isPresent()) {
            SeatHold hold = seatHold.get();
            hold.reserve();
            seatHolds.remove(hold);
            code = confirmationCode(seatHoldId, customerEmail);
        }

        return code;
    } // reserveSeats()

    /**
     * Create confirmation code, based upon hold id and customer
     * @param seatHoldId the SeatHold identifier
     * @param customerEmail address of the purchaser
     * @return the confirmation code
     */
    protected String confirmationCode(int seatHoldId, String customerEmail) {
        return customerEmail + "--" + seatHoldId;
    }

    /**
     * Return held seats to available seats
     * @param seatHold SeatHold object containing the seats
     */
    public void releaseHold(SeatHold seatHold) {
        // Release all the seats, as an atomic transaction.
        synchronized (this) {
            Collection<Seat> seats = seatHold.getSeats();
            seats.stream().forEach( seat -> seat.requestRelease() );
            seatHolds.remove(seatHold);
        }
    } // releaseHold()

    /**
     * Clean up routine, interrupts the expiration management thread,
     * required to satified the AutoCloseable interface
     */
    public void close() {
        sweeper.ifPresent(thread -> thread.interrupt());
    } // close()

    /**
     * Releases any seat holds that have an expiration date-time less
     * than or equal to the current date-time
     *
     */
    protected void expire() {
        long now = (new Date()).getTime();

        synchronized (seatHolds) {
            seatHolds.stream()
                .filter(hold -> now > hold.getExpiration())
                .forEach(hold -> releaseHold(hold));
        }
    } // expire()

    /**
     * Internal Thread class that sleeps for a 1 second, then fires
     * the expire function
     */
    private class Sweeper extends Thread {
        public void run() {
            while (true) {
                try {
                    sleep(1000); // one second
                    expire();
                }
                catch (InterruptedException e) {
                    break;
                }
            }
        }
    } // private class Sweeper

}
