// author: Mark W. Naylor
// file:   SeatHold.java
// date:   2016-Aug-29

package com.walmart.homework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.ListIterator;
import java.util.Optional;



public class SeatHold {
    private static int lastId = 0;

    private int id;
    private String customerEmail;
    private ArrayList<Seat> seats  = new ArrayList<Seat>();
    private long expiration;

    public SeatHold (String customerEmail, Optional<Collection<Seat>> seats) {
        synchronized(this) {
            id = createId();
        }
        this.customerEmail = customerEmail;

        if (seats.isPresent()) {
            this.seats.addAll(seats.get());
        }

        expiration = (new Date()).getTime() + Defaults.SEATHOLD_LIFESPAN;
    } // SeatHold() - constructor

    public void add(Seat seat) {
        seats.add(seat);
    } // add()

    public void add(Collection<Seat> seats) {
        this.seats.addAll(seats);
    } // add()

    public void release() {
        seats.stream().forEach(seat -> seat.requestRelease());
        remove();
    } // release()

    public void reserve() {
        seats.stream().forEach(seat -> seat.requestReserve());
        remove();
    } // reserve()

    protected void remove() {
        for (ListIterator<Seat> seatIterator = seats.listIterator(); seatIterator.hasNext();) {
            seatIterator.next();
            seatIterator.remove();
        }
    } // remove()

    public int size() {
        return seats.size();
    }

    public Collection<Seat> getSeats() {
        return seats;
    } // getSets()

    public int getId() {
        return id;
    } // getId()

    public String getCustomerEmail() {
        return customerEmail;
    } // getCustomerEmail()

    public long getExpiration() {
        return expiration;
    }

    protected int createId() {
        ++lastId;
        return lastId;
    } // createId()
}
