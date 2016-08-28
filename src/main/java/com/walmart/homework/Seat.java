// author: Mark W. Naylor
// file:   Seat.java
// date:   2016-Aug-27

package com.walmart.homework;

public class Seat {
    private RowNumber rowNumber;
    private SeatNumber seatNumber;
    private MonetaryAmount price;
    private LevelName level;

    public Seat (RowNumber rowNumber,
                 SeatNumber seatNumber,
                 MonetaryAmount price,
                 LevelName level) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.price = price;
        this.level = level;
    }

    public RowNumber getRowNumber() {
        return rowNumber;
    }

    public SeatNumber getSeatNumber() {
        return seatNumber;
    }

    public MonetaryAmount getPrice() {
        return price;
    }
}
