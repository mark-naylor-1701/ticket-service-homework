// author: Mark W. Naylor
// file:   Level.java
// date:   2016-Aug-26

package com.walmart.homework;

import java.util.ArrayList;

public class Level {
    private int id;
    private LevelName name;
    private MonetaryAmount price;
    private int rows;
    private int seatsPerRow;

    public ArrayList<Seat> remainingSeats;
    public ArrayList<Seat> heldSeats = new ArrayList<Seat>();
    public ArrayList<Seat> reservedSeats = new ArrayList<Seat>();

    public Level(int id, LevelName name, MonetaryAmount price, int rows, int seatsPerRow) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;

        remainingSeats = makeSeats();
    } // Level() - constructor

    public ArrayList<Seat> holdNumberOfSeats(int desired) {
        int maxIndex = Math.min(desired, numSeatsAvailable());
        ArrayList<Seat> held = new ArrayList<Seat>(remainingSeats.subList(0, maxIndex));

        held.stream().forEach( seat-> holdSeat(seat) );

        return held;
    } // holdNumberOfSeats()

    protected void moveSeat(Seat seat, ArrayList<Seat> to, ArrayList<Seat> from) {
        synchronized (this) {
            to.add(seat);
            from.remove(seat);
        }
    } // moveSeat

    public void holdSeat(Seat seat) {
        moveSeat(seat, heldSeats, remainingSeats);
    } // holdSeat()

    public void releaseSeat(Seat seat) {
        moveSeat(seat, remainingSeats, heldSeats);
    } // releaseSeat()

    public void reserveSeat(Seat seat) {
        moveSeat(seat, reservedSeats, heldSeats);
    } // reservedSeat()

    public String toString() {
        return id + "\t" + String.format("%-8s", name) + "\t"
            + price + "\t" + numSeatsAvailable();
    } // toString()

    public int getId() {
        return id;
    } // getId()

    public LevelName getName() {
        return name;
    } // getName()

    public MonetaryAmount getPrice() {
        return price;
    } // getPrice()

    public int numSeatsAvailable() {
        return remainingSeats.size();
    } // numSeatsAvailable()

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
