package com.walmart.homework;

import java.util.Optional;

import static org.junit.Assert.*;

import org.junit.Test;

public class VenueTest {
    private Optional<Integer> noneInt = Optional.empty();
    private String email = "mark.naylor.1701@gmail.com";

    @Test
    public final void test() {
        try (Venue tv = Venue.testingVenue()) {
            assertNotNull("No Venue returned.\n", tv);

            int initialSeats = tv.numSeatsAvailable(noneInt);
            assertEquals("Wrong number of initial seats.\n", 40, initialSeats);

            int holdCount = tv.holdCount();
            assertEquals("Wrong number of seat holds.\n", 0, holdCount);
        }
    }

    @Test
    public final void testVenueHoldExceedCapacity() {
        try (Venue tv = Venue.testingVenue()) {
            int seatRequest = 15;
            int capacity = tv.numSeatsAvailable(noneInt);

            Optional<SeatHold> seatHold =
                tv.findAndHoldSeats(seatRequest, noneInt, Optional.of(1), email);
            assertFalse("No seat hold should have been returned", seatHold.isPresent());
        }
    }

    @Test
    public final void testVenueHoldInCapacity() {

        try (Venue tv = Venue.testingVenue()) {
            int seatRequest = 5;
            int capacity = tv.numSeatsAvailable(noneInt);
            int expected = capacity - seatRequest;

            tv.findAndHoldSeats(seatRequest, noneInt, Optional.of(1), email);
            int seatsAvail = tv.numSeatsAvailable(noneInt);
            assertEquals("Wrong number of held seats.\n" ,seatsAvail, expected);
        }
    }

    @Test
    public final void testSeatHoldMeetCapacityExplicitRelease() {

        try (Venue tv = Venue.testingVenue()) {
            int seatRequest = 5;
            int capacity = tv.numSeatsAvailable(noneInt);
            int expected = capacity - seatRequest;

            Optional<SeatHold> seatHold =
                tv.findAndHoldSeats(seatRequest, noneInt, Optional.of(1), email);
            int seatsAvail = tv.numSeatsAvailable(noneInt);
            assertEquals("Wrong number of available seats.\n", expected, seatsAvail);

            seatHold.ifPresent(sh -> tv.releaseHold(sh));

            seatsAvail = tv.numSeatsAvailable(noneInt);
            assertEquals("Wrong number of seats after release.\n", capacity, seatsAvail);
        }
    }

    @Test
    public final void testSeatHoldMeetCapacityTimeOutRelease() {
        try (Venue tv = Venue.testingVenue()) {
            int seatRequest = 5;
            int capacity = tv.numSeatsAvailable(noneInt);
            int expected = capacity - seatRequest;

            Optional<SeatHold> seatHold =
                tv.findAndHoldSeats(seatRequest, noneInt, Optional.of(1), email);
            int  seatsAvail = tv.numSeatsAvailable(noneInt);
            assertEquals("Wrong number of available seats", expected, seatsAvail);

            try {
                System.out.println("Pause to let hold expire.");
                Thread.sleep(Defaults.SEATHOLD_LIFESPAN * 2);
                System.out.println("Hold should have expired.");
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            seatsAvail = tv.numSeatsAvailable(noneInt);
            assertEquals("Wrong number of available seats after timed release.\n",
                         capacity,
                         seatsAvail);
        }
    }

    @Test
    public final void testVenueSeatReserve() {
        try (Venue tv = Venue.testingVenue()) {
            int seatRequest = 5;
            int capacity = tv.numSeatsAvailable(noneInt);
            int expected = capacity - seatRequest;

            Optional<SeatHold> seatHold =
                tv.findAndHoldSeats(seatRequest, noneInt, Optional.of(1), email);
            int seatsAvail = tv.numSeatsAvailable(noneInt);
            assertEquals("Wrong number of holds.\n", expected, seatsAvail);

            seatHold.ifPresent(sh -> {
                    int id = sh.getId();
                    String address = sh.getCustomerEmail();
                    tv.reserveSeats(id, address);
                });

            int holdCount = tv.holdCount();
            assertEquals("Wrong number of holds after reserve.\n", 0, holdCount);

            seatsAvail = tv.numSeatsAvailable(noneInt);
            assertEquals("After reserve, wrong number of available seats.\n",
                         expected,
                         seatsAvail);

        }
    }

    @Test
    public final void testVenueMultiSeatReserve() {
        try (Venue tv = Venue.testingVenue()) {
            int seatRequest = 5;
            int capacity = tv.numSeatsAvailable(noneInt);
            int expected = capacity - seatRequest;

            Optional<SeatHold> hold = tv.findAndHoldSeats(seatRequest, noneInt, Optional.of(1), email);
            int seatsAvail = tv.numSeatsAvailable(noneInt);
            //assert(seatsAvail == expected, s"Before reserve #1, wrong # of holds: ${seatsAvail} != ${expected}.\n");
            assertEquals("Wrong number of seats, before first reserve.\n", expected, seatsAvail);

            int holds = tv.holdCount();
            //assert(holds == 1, s"${holds} != ${1}\n");
            assertEquals("Wrong number of holds, before first reserve.\n", 1, holds);

            // hold map {
            //     hold => {
            //         val id = hold.getId;
            //         val address = hold.getCustomerEmail;
            //         tv.reserveSeats(id, address)}}

            hold.ifPresent(h -> {
                    int id = h.getId();
                    String address = h.getCustomerEmail();
                    tv.reserveSeats(id, address);});

            holds = tv.holdCount();
            //assert(holds == 0, s"After reserve #1, wrong # of holds: ${holds} != ${0}\n");
            assertEquals("Wrong number of holds, after first reserve.\n", 0, holds);

            seatsAvail = tv.numSeatsAvailable(noneInt);
            //assert(seatsAvail == expected, s"After reserve #1, wrong # of seats: ${seatsAvail} != ${expected}.\n");
            assertEquals("Wrong number of seats, after first reserve.\n", expected, seatsAvail);

            seatRequest = 20;
            expected -= seatRequest;
            hold = tv.findAndHoldSeats(seatRequest, noneInt, noneInt, email);

            seatsAvail = tv.numSeatsAvailable(noneInt);
            //assert(seatsAvail == expected, s"Before reserve #2, wrong # of holds: ${seatsAvail} != ${expected}.\n");
            assertEquals("Before second reserve, wrong number of seats.\n", expected, seatsAvail);

            holds = tv.holdCount();
            //assert(holds == 1, s"${holds} != ${1}\n");
            assertEquals("Before second reserve, wrong number of holds.\n", 1, holds);

            // hold map {
            //     hold => {
            //         val id = hold.getId;
            //         val address = hold.getCustomerEmail;
            //         tv.reserveSeats(id, address)}}

            hold.ifPresent(h -> {
                    int id = h.getId();
                    String address = h.getCustomerEmail();
                    tv.reserveSeats(id, address);});

            holds = tv.holdCount();
            //assertEquals(holds == 0, s"After reserve #2, wrong # of holds: ${holds} != ${0}\n");
            assertEquals("After second reserve, wrong number of holds.\n", 0, holds);

            seatsAvail = tv.numSeatsAvailable(noneInt);
            assertEquals("Wrong number of seats, after second reserve.\n", expected, seatsAvail);

        }
    }

}
