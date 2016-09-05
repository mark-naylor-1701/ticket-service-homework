// author: Mark W. Naylor
// file:   SeatHold.java
// date:   2016-Aug-29

package com.walmart.homework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.ListIterator;
import java.util.Optional;


/**
 * The SeatHold class manages the information for customer requested seats.
 */
public class SeatHold {
    private static int lastId = 0;

    private int id;
    private String customerEmail;
    private ArrayList<Seat> seats  = new ArrayList<Seat>();
    private long expiration;

    /**
     * Construct a new SeatHold object
     * @param customerEmail address of person purchasing seats
     * @param seats optional collection of seats to track in the SeatHold 
     */
    public SeatHold (String customerEmail, Optional<Collection<Seat>> seats) {
        synchronized(this) {
            id = createId();
        }
        this.customerEmail = customerEmail;

        seats.ifPresent( sts -> add(sts) );

        expiration = (new Date()).getTime() + Defaults.SEATHOLD_LIFESPAN;
    } // SeatHold() - constructor

    /**
     * Adds a single Seat to the SeatHold
     * @param seat the Seat to be added
     */
    public void add(Seat seat) {
        seats.add(seat);
    } // add()

    /**
     * Add a collection of Seats to the SeatHold
     * @param seats the Seat collection 
     */
    public void add(Collection<Seat> seats) {
        this.seats.addAll(seats);
    } // add()

    /**
     * Release held Seats
     */
    public void release() {
        seats.stream().forEach(seat -> seat.requestRelease());
        remove();
    } // release()

    /**
     * Reserve held Seats
     */
    public void reserve() {
        seats.stream().forEach(seat -> seat.requestReserve());
        remove();
    } // reserve()

    /**
     * Empty the SeatHold of the Seats it is tracking
     */
    protected void remove() {
        for (ListIterator<Seat> seatIterator = seats.listIterator(); seatIterator.hasNext(); ) {
            seatIterator.next();
            seatIterator.remove();
        }
    } // remove()

    /**
     * Returns the size of the SeatHold
     * @return the number of Seats
     */
    public int size() {
        return seats.size();
    }

    /**
     * Return a collection of held seats
     * @return the Seats
     */
    public Collection<Seat> getSeats() {
        return seats;
    } // getSets()

    /**
     * Returns the SeatHold identifier
     * @return the identifier number
     */
    public int getId() {
        return id;
    } // getId()

    /**
     * Returns email addres of the person assigned to the SeatHold 
     * @return the email address
     */
    public String getCustomerEmail() {
        return customerEmail;
    } // getCustomerEmail()

    /**
     * Returns the SeatHold expiration.  This is equivalent to the
     * getTime() function of the java.util.Date class.
     * @return the expiration time
     */
    public long getExpiration() {
        return expiration;
    }

    /**
     * Make and return a new SeatHold identifier
     * @return the new Id number
     */
    protected int createId() {
        ++lastId;
        return lastId;
    } // createId()
}
