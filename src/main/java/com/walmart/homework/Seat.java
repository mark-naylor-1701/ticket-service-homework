// author: Mark W. Naylor
// file:   Seat.java
// date:   2016-Aug-27

package com.walmart.homework;

/**
 * The Seat class encapsulates the location information for a seat.
 * In addtion, a Seat contains a reference to its parent Level, to
 * facilitate hold release and reservation activities.
 */
public class Seat {
    private RowNumber rowNumber;
    private SeatNumber seatNumber;
    private Level level;

    /**
     * Constructs a Seat object. 
     * @param rowNumber the row where the seat is located
     * @param seatNumber the seat's number with in its row
     * @param level the parent level of the seat
     */
    public Seat (RowNumber rowNumber,
                 SeatNumber seatNumber,
                 Level level) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.level = level;
    } // Seat() - constructor

    /**
     * Returns the RowNumber object
     * @return RowNumber object
     */
    public RowNumber getRowNumber() {
        return rowNumber;
    } // getRowNumber()

    /**
     * Returns the SeatNumber object
     * @return SeatNumber object
     */
    public SeatNumber getSeatNumber() {
        return seatNumber;
    } // getSeatNumber()

    /**
     * Returns the MonetaryAmount object
     * @return MonetaryAmount object
     */
    public MonetaryAmount getPrice() {
        return level.getPrice();
    } // getPrice()

    /**
     * Returns the Level object
     * @return Level object
     */
    public Level getLevel() {
        return level;
    } // getLevel()

    /**
     * Sends a message to its parent level to release the hold on it
     */
    public void requestRelease() {
        level.releaseSeat(this);
    } // requestRelease()

    /**
     * Sends a message to its parent level to reserve it
     */
    public void requestReserve() {
        level.reserveSeat(this);
    } // requestReserve()
}
