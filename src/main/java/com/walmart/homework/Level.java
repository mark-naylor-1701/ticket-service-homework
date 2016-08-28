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

    private ArrayList<Seat> remainingSeats;
    private ArrayList<Seat> heldSeats = new ArrayList<Seat>();
    private ArrayList<Seat> reservedSeats = new ArrayList<Seat>();

    public Level(int id, LevelName name, MonetaryAmount price, int rows, int seatsPerRow) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;

        remainingSeats = makeSeats();
    }

    public String toString() {
        return id + "\t" + String.format("%-8s", name) + "\t" + price + "\t" + remainingSeats;
    }

    public int getId() {
        return id;
    }

    public LevelName getName() {
        return name;
    }

    public MonetaryAmount getPrice() {
        return price;
    }

    public int remainingSeatCount() {
        return remainingSeats.size();
    }


    private ArrayList<Seat> makeSeats() {
        ArrayList<Seat> seats = new ArrayList<Seat>(rows * seatsPerRow);
        for (int row = 1; row <= rows; row++) {
            RowNumber rowNumber = new RowNumber(row);
            for (int seat = 1; seat <= seatsPerRow; seat++) {
                Seat newSeat = new Seat(rowNumber, new SeatNumber(seat), price, name);
                seats.add(newSeat);
            }
        }
        return seats;
    }
}
