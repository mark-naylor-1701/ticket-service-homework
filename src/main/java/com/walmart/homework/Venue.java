// author: Mark W. Naylor
// file:   Venue.java
// date:   2016-Aug-28

package com.walmart.homework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;


import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Venue implements TicketService {

    // *TODO* make this private
    /*private*/ public ArrayList<Level> levels;

    public Venue () {
        levels = new ArrayList<Level>();
    } // Venue() - constructor

    public Venue (Collection<Level> collection) {
        levels = new ArrayList<Level>(collection);
    } // Venue() - constructor


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
            int result = levelStream.filter(level -> level.getId() == venueLevel.get())
                .map(level -> level.numSeatsAvailable())
                .findFirst()
                .orElse(0);
            //System.out.println(result);
            return result;
        } else {
            int result = levelStream.mapToInt(level -> level.numSeatsAvailable())
                .sum();
            //System.out.println(result);
            return result;
        }

    } // numSeatsAvailable()

    public int numSeatsAvailable(Collection<Level> levels) {
        Stream<Level> levelStream = levels.stream();
        return levelStream.mapToInt(level -> level.numSeatsAvailable())
            .sum();
    } // numSeatsAvailable()

    public Optional<SeatHold> findAndHoldSeats(int numSeats,
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

        // System.out.println("Acceptable levels:" + levelCount);
        // System.out.println("Available seats:  " + seatCount);

        if (numSeats > seatCount) { return Optional.empty(); }

        SeatHold seatHold = new SeatHold(customerEmail, Optional.empty());
        ArrayList<Seat> seats;

        Iterator<Level> iterator = desiredLevels.iterator();

        while (numSeats > 0 && iterator.hasNext()) {
            Level level = iterator.next();
            seats = level.holdSeats(numSeats);
            seatHold.add(seats);
            numSeats -= seats.size();
        }

        System.out.println("# of seats:    " + numSeats);
        System.out.println("more iterator? " + iterator.hasNext());

        return Optional.of(seatHold);
    } // findAndHoldSeats()

    public void releaseHold(SeatHold seatHold) {
        Collection<Seat> seats = seatHold.getSeats();

        // Release all the seats, as an atomic transaction.
        synchronized (this){
            // Each seat needs to tell its parent level to release it.
            seats.stream().forEach((Seat s) -> s.getLevel().releaseHold(s));
        }
    } // releaseHold

}
