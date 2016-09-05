// author: Mark W. Naylor
// file:   Level.java
// date:   2016-Aug-26

package com.walmart.homework;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The Level class manages a collection of seats.  Constructs
 * seats and tracks their states: available, held, or reserved.
 */
public class Level {
    private int id;
    private LevelName name;
    private MonetaryAmount price;
    private int rows;
    private int seatsPerRow;

    /**
     * Available seats, neither held nor reserved
     */
    private ArrayList<Seat> remainingSeats;
    /**
     * Seats that have been requested, but do not yet have a committed reservation
     */
    private ArrayList<Seat> heldSeats = new ArrayList<Seat>();
    /**
     * Seats that have been reserved
     */
    private ArrayList<Seat> reservedSeats = new ArrayList<Seat>();

    /**
     * Constructs a new Level object.
     * @param id the number identifying the level
     * @param name the name of the level
     * @param price the cost of seats in the level
     * @param rows the number of rows in the level
     * @param seatsPerRow the number of seats in each row
     */
    public Level(int id, LevelName name, MonetaryAmount price, int rows, int seatsPerRow) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;

        remainingSeats = makeSeats();
    } // Level() - constructor

    /**
     * If there are enough remaining seats in the to meet the request,
     * put them on hold and reture the colleaction.  Otherwise, hold
     * and return the remaining seats in the level
     * @param desired the number of seats requested
     * @return a collection of seats that were held, the collection may be empty
     */
    public Collection<Seat> holdNumberOfSeats(int desired) {
        int maxIndex = Math.min(desired, numSeatsAvailable());
        ArrayList<Seat> held = new ArrayList<Seat>(remainingSeats.subList(0, maxIndex));

        held.stream().forEach( seat-> holdSeat(seat) );

        return held;
    } // holdNumberOfSeats()

    /**
     * Helper function to move a Seat object from one ArrayList to another.
     * @param seat the seat to be moved
     * @param to the destination ArrayList
     * @param from the source ArrayList 
     */
    protected void moveSeat(Seat seat, ArrayList<Seat> to, ArrayList<Seat> from) {
        synchronized (this) {
            to.add(seat);
            from.remove(seat);
        }
    } // moveSeat

    /**
     * Takes a seat and puts it on hold
     * @param seat the seat to be held 
     */
    public void holdSeat(Seat seat) {
        // from available to held seats
        moveSeat(seat, heldSeats, remainingSeats);
    } // holdSeat()

    /**
     * Takes a held seat and makes it available
     * @param seat the seat to released
     */
    public void releaseSeat(Seat seat) {
        // from held to available seats
        moveSeat(seat, remainingSeats, heldSeats);
    } // releaseSeat()

    /**
     * Takes a held seat and reserves it
     * @param seat the seat to reserved
     */
    public void reserveSeat(Seat seat) {
        // from held to reserved seats
        moveSeat(seat, reservedSeats, heldSeats);
    } // reservedSeat()

    /**
     * Overrides the inherited toString function
     * @return Returns a String object representing this Level's value
     */
    @Override
    public String toString() {
        return id + "\t" + String.format("%-8s", name) + "\t"
            + price + "\t" + numSeatsAvailable();
    } // toString()

    /**
     * Returns the Level's identifier
     * @return the id number
     */
    public int getId() {
        return id;
    } // getId()

    /**
     * Returns the name of the Level
     * @return a LevelName object
     */
    public LevelName getName() {
        return name;
    } // getName()

    /**
     * Returns the price of seats in the Level
     * @return a MonetaryAmount object
     */
    public MonetaryAmount getPrice() {
        return price;
    } // getPrice()

    /**
     * Returns count of the Level's remaining seats
     * @return the number of remaining seats
     */
    public int numSeatsAvailable() {
        return remainingSeats.size();
    } // numSeatsAvailable()

    /**
     * Give the information supplied to the Level constructor, creates the seats for the level.
     * @return an ArrayList of Seats
     */
    private ArrayList<Seat> makeSeats() {
        ArrayList<Seat> seats = new ArrayList<Seat>(rows * seatsPerRow);
        for (int row = 1; row <= rows; row++) {
            RowNumber rowNumber = new RowNumber(row);
            for (int seat = 1; seat <= seatsPerRow; seat++) {
                Seat newSeat = new Seat(rowNumber, new SeatNumber(seat), this);
                seats.add(newSeat);
            }
        }
        return seats;
    } // makeSeats()
}
