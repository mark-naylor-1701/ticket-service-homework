// author: Mark W. Naylor
// file:   SeatHold.java
// date:   2016-Aug-29

package com.walmart.homework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;



public class SeatHold {
    static int lastId = 0;

    private int id;
    private String customerEmail;
    private Collection<Seat> seats  = new ArrayList<Seat>();

    public SeatHold (String customerEmail, Optional<Collection<Seat>> seats) {
        synchronized(this) {
            id = createId();
        }
        this.customerEmail = customerEmail;

        if (seats.isPresent()) {
            this.seats.addAll(seats.get());
        }
    } // SeatHold() - constructor

    public void add(Seat seat) {
        seats.add(seat);
    } // add()

    public void add(Collection<Seat> seats) {
        this.seats.addAll(seats);
    } // add()

    public Collection getSeats() {
        return seats;
    } // getSets()

    public int getId() {
        return id;
    } // getId()

    public String getCustomerEmail() {
        return customerEmail;
    } // getCustomerEmail()

    private int createId() {
        ++lastId;
        return lastId;
    } // createId()
}
