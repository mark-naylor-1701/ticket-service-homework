package com.walmart.homework;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

import org.junit.Test;

public class SeatHoldTest {
    private String email = "mark.naylor.1701@gmail.com";

    @Test
    public final void testSeatHold() {
        Optional<Collection<Seat>> none = Optional.empty();
        SeatHold seatHold = new SeatHold(email, none);

        Date now = new Date();
        long expiration = seatHold.getExpiration();
        assertTrue("Expiration not greater than current time.\n", expiration > now.getTime());

        int seatCount = seatHold.getSeats().size();
        assertEquals("No seats should be held at creation.\n",
                     0, seatCount);

        String resEmail = seatHold.getCustomerEmail();
        assertTrue("Emails do not match.\n", email.equals(resEmail));

    }

    @Test
    public final void testHoldAndRelease() {
        Level level = LevelTest.makeLevel();
        RowNumber rowNumber = new RowNumber(1);
        int expectedAvailableSeats = level.numSeatsAvailable();
        int desiredSeats = 5;

        Collection<Seat> seats = level.holdNumberOfSeats(desiredSeats);

        SeatHold seatHold = new SeatHold(email, Optional.of(seats));

        assertEquals("Wrong number of seats in the hold.\n",
                     seatHold.size(),
                     desiredSeats);


        seatHold.release();

        assertEquals("Wrong number of held seats after release.\n", 0, seatHold.size());
        assertEquals("Wrong number of available seats after release.\n",
                     expectedAvailableSeats,
                     level.numSeatsAvailable());

    }

    @Test
    public final void testReserve() {
        Level level = LevelTest.makeLevel();
        RowNumber rowNumber = new RowNumber(1);
        int expectedAvailableSeats = level.numSeatsAvailable();
        int desiredSeats = 5;
        int expectedAvailablePostRelease = expectedAvailableSeats - desiredSeats;

        Collection<Seat> seats = level.holdNumberOfSeats(desiredSeats);

        SeatHold seatHold = new SeatHold(email, Optional.of(seats));

        assertEquals("Wrong number of seats in the hold.\n",
                     seatHold.size(),
                     desiredSeats);


        seatHold.reserve();

        assertEquals("Wrong number of held seats after reserve.\n", 0, seatHold.size());
        assertEquals("Wrong number of available seats after reserve.\n",
                     expectedAvailablePostRelease,
                     level.numSeatsAvailable());
    }

}
