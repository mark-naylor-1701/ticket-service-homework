package com.walmart.homework;

import java.util.Collection;

import static org.junit.Assert.*;

import org.junit.Test;

public class LevelTest {
    private static LevelName name = new LevelName("Test Name");
    private static MonetaryAmount price = new MonetaryAmount(15.50);
    private static int rows = 4;
    private static int seats = 10;
    private static int id = 1;


    @Test
    public final void testLevel() {
        Level level = makeLevel();
        assertNotNull("Level not created.\n", level);
    }

    @Test
    public final void testHoldNumberOfSeats() {
        Level level = makeLevel();
        int seatsRequested = 5;
        int intialCount = level.numSeatsAvailable();
        int expected = intialCount - seatsRequested;

        level.holdNumberOfSeats(seatsRequested);

        int count = level.numSeatsAvailable();
        assertEquals("Wrong number of seats after hold request.\n", expected, count);
    }

    @Test
    public final void testNumSeatsAvailable() {
        Level level = makeLevel();
        assertEquals("Wrong number of seats after initialization.\n",
                     level.numSeatsAvailable(),
                     rows * seats);
    }


    @Test
    public final void testLevelReleaseSeats() {
        Level level = makeLevel();
        int seatsRequired = 5;
        int initialCount = level.numSeatsAvailable();
        int expected = initialCount - seatsRequired;
        Collection<Seat> seats = level.holdNumberOfSeats(seatsRequired);

        int seatsAvailable = level.numSeatsAvailable();
        assertEquals("Wrong number of seats after hold request.\n",
                     seatsAvailable,
                     initialCount - seatsRequired);

        // seats.asScala.map(level.releaseSeat(_));
        seats.stream().forEach( seat -> level.releaseSeat(seat) );
        seatsAvailable = level.numSeatsAvailable();
        assertEquals("Wrong number of seats after hold release.\n", seatsAvailable, initialCount);
    }


    @Test
    public final void testLevelReserve() {
        Level level = makeLevel();
        int seatsRequired = 5;
        int initialCount = level.numSeatsAvailable();
        Collection<Seat> seats = level.holdNumberOfSeats(seatsRequired);
        int expected = initialCount - seatsRequired;

        int count = level.numSeatsAvailable();
        assertEquals("Wrong number of seats after hold.\n", count, expected);

        seats.stream().forEach( seat -> level.reserveSeat(seat) );
        count = level.numSeatsAvailable();
        assertEquals("Wrong number of seats after release.\n", count, expected);

    }

    public static Level makeLevel() {
        return new Level(id, name, price, rows, seats);
    }

}
