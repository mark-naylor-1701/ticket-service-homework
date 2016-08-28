// author: Mark W. Naylor
// file:   Venue.java
// date:   2016-Aug-28

package com.walmart.homework;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Venue implements TicketService {

    private ArrayList<Level> levels;

    public Venue () {
        levels = new ArrayList<Level>();
    }

    public Venue (Collection<Level> collection) {
        levels = new ArrayList<Level>(collection);
    }


    public static Venue defaultVenue() {
        Level l1 = new Level(1, new LevelName("Orchestra"), new MonetaryAmount(100.00), 25, 50);
        Level l2 = new Level(2, new LevelName("Main"), new MonetaryAmount(75.00), 20, 100);
        Level l3 = new Level(3, new LevelName("Balcony 1"), new MonetaryAmount(50.00), 15, 100);
        Level l4 = new Level(4, new LevelName("Balcony 2"), new MonetaryAmount(40.00), 15, 100);

        return new Venue(Arrays.asList(l1, l2, l3, l4));
    }

    public static Venue testingVenue() {
        Level l1 = new Level(1, new LevelName("Orchestra"), new MonetaryAmount(100.00), 1, 10);
        Level l2 = new Level(2, new LevelName("Main"), new MonetaryAmount(75.00), 1, 10);
        Level l3 = new Level(3, new LevelName("Balcony 1"), new MonetaryAmount(50.00), 1, 10);
        Level l4 = new Level(4, new LevelName("Balcony 2"), new MonetaryAmount(40.00), 1, 10);

        return new Venue(Arrays.asList(l1, l2, l3, l4));
    }

    public int numSeatsAvailable(Optional<Integer> venueLevel) {
        Stream<Level> levelStream = levels.stream();
        if (venueLevel.isPresent()) {
            int result = levelStream.filter(l -> l.getId() == venueLevel.get())
                .map(m -> m.remainingSeatCount())
                .findFirst()
                .orElse(0);
            //System.out.println(result);
            return result;
        } else {
            int result = levelStream.mapToInt(l -> l.remainingSeatCount())
                .sum();
            //System.out.println(result);
            return result;
        }

    }

    private int allLevelSeats() {
        return numSeatsAvailable(Optional.of(1)) +
            numSeatsAvailable(Optional.of(2)) +
            numSeatsAvailable(Optional.of(3)) +
            numSeatsAvailable(Optional.of(4));
    }
}
