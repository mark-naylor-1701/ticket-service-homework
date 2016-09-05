// author: Mark W. Naylor
// file:   SeatNumber.java
// date:   2016-Aug-27

package com.walmart.homework;

/**
 * Wraps an Integer to create an explicit SeatNumber.
 */

public class SeatNumber extends TypeExtender<Integer> {
    /**
     * Constructs a new SeatNumber with the specified seatnumber.
     * @param seatNumber the value of the seat number
     *
     */
    public SeatNumber(Integer seatNumber) {
        super(seatNumber);
    }
}
