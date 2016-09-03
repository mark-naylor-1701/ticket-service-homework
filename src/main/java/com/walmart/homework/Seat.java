// author: Mark W. Naylor
// file:   Seat.java
// date:   2016-Aug-27

package com.walmart.homework;

public class Seat {
    private RowNumber rowNumber;
    private SeatNumber seatNumber;
    private Level level;

    public Seat (RowNumber rowNumber,
                 SeatNumber seatNumber,
                 Level level) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.level = level;
    } // Seat() - constructor

    public RowNumber getRowNumber() {
        return rowNumber;
    } // getRowNumber()

    public SeatNumber getSeatNumber() {
        return seatNumber;
    } // getSeatNumber()

    public MonetaryAmount getPrice() {
        return level.getPrice();
    } // getPrice()

    public Level getLevel() {
        return level;
    } // getLevel()

    public void requestRelease() {
        level.releaseSeat(this);
    } // requestRelease()

    public void requestReserve() {
        level.reserveSeat(this);
    } // requestReserve()
}
