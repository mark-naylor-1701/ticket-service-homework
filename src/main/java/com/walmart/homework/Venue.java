// author: Mark W. Naylor
// file:   Venue.java
// date:   2016-Aug-28

package com.walmart.homework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import java.util.concurrent.CopyOnWriteArrayList;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Venue implements TicketService {

    private ArrayList<Level> levels;
    private CopyOnWriteArrayList<SeatHold> heldSeats = new CopyOnWriteArrayList<SeatHold>();
    private Optional<Sweeper> sweeper = Optional.empty();

    public Venue () {
        levels = new ArrayList<Level>();
        startSweeper();
    } // Venue() - constructor

    public Venue (Collection<Level> collection) {
        levels = new ArrayList<Level>(collection);
        startSweeper();
    } // Venue() - constructor

    private void startSweeper() {
        Sweeper thread = new Sweeper();
        thread.start();
        sweeper = Optional.of(thread);
    } // startSweeper

    public static Venue defaultVenue() {
        Level l1 = new Level(1, new LevelName("Orchestra"), new MonetaryAmount(100.00), 25, 50);
        Level l2 = new Level(2, new LevelName("Main"), new MonetaryAmount(75.00), 20, 100);
        Level l3 = new Level(3, new LevelName("Balcony 1"), new MonetaryAmount(50.00), 15, 100);
        Level l4 = new Level(4, new LevelName("Balcony 2"), new MonetaryAmount(40.00), 15, 100);

        return new Venue(Arrays.asList(l1, l2, l3, l4));
    } // defaultVenue()

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

        if (minLevel.isPresent()) {
            stream = stream.filter( n -> n.getId() >= minLevel.get() );
        }
        if (maxLevel.isPresent()) {
            stream = stream.filter( n -> n.getId() <= maxLevel.get() );
        }

        ArrayList<Level> desiredLevels = new ArrayList<Level>(stream.collect(Collectors.toList()));
        int levelCount = desiredLevels.size();
        int seatCount = numSeatsAvailable(desiredLevels);

        if (requiredSeats > seatCount) { return Optional.empty(); }

        SeatHold seatHold = new SeatHold(customerEmail, Optional.empty());

        for (Iterator<Level> levels = desiredLevels.iterator(); requiredSeats > 0 && levels.hasNext(); ) {
            Level level = levels.next();
            ArrayList<Seat> seats = level.holdNumberOfSeats(requiredSeats);
            seatHold.add(seats);
            requiredSeats -= seats.size();
        }

        heldSeats.add(seatHold);

        return Optional.of(seatHold);
    } // findAndHoldSeats()

    public int holdCount() {
        return heldSeats.size();
    } // holdCount()

    public String reserveSeats(int seatHoldId, String customerEmail) {

        // Find hold with id
        // Loop through the seats
        // Tell the level to reserve each seat

        // craft the comp code
        return "*TODO*";
    } // reserveSeats()


    public void releaseHold(SeatHold seatHold) {
        Collection<Seat> seats = seatHold.getSeats();

        // Release all the seats, as an atomic transaction.
        synchronized (this) {
            seats.stream().forEach( seat -> seat.requestRelease() );
            heldSeats.remove(seatHold);
        }
    } // releaseHold()

    public void close() {
        sweeper.ifPresent(thread -> thread.interrupt());
    } // close()


    protected void expire() {
        long now = (new Date()).getTime();

        synchronized (heldSeats) {
            heldSeats.stream()
                .filter(hold -> now > hold.getExpiration())
                .forEach(hold -> releaseHold(hold));
        }
    } // expire()


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

                System.out.println(getName() +" wake and process");
            }
        }
    } // private class Sweeper

}
