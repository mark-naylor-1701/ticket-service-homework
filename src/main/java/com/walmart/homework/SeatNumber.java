// author: Mark W. Naylor
// file:   SeatNumber.java
// date:   2016-Aug-27

package com.walmart.homework;

public class SeatNumber extends TypeExtender<Integer> {
    private Integer seatNumber;

    public SeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Integer value() {
        return seatNumber;
    }
}
