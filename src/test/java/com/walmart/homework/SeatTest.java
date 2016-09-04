package com.walmart.homework;

import static org.junit.Assert.*;

import org.junit.Test;

public class SeatTest {

    @Test
    public final void testSeat() {
        RowNumber rowNumber = new RowNumber(1);
        SeatNumber seatNumber = new SeatNumber(2);
        Seat seat = new Seat(rowNumber, seatNumber, null);
        assertNotNull("No seat created.\n", seat);
    }

}
