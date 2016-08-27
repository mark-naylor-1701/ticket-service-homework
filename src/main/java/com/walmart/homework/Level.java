// author: Mark W. Naylor
// file:   Level.java
// date:   2016-Aug-26

package com.walmart.homework;

public class Level {
    private int id;
    private String name;
    private double price;
    private int rows;
    private int seatsInRow;
    private int seats;
    private int remainingSeats;


    public Level(int id, String name, double price, int rows, int seatsInRow) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rows = rows;
        this.seatsInRow = seatsInRow;
        this.seats = this.remainingSeats = rows * seatsInRow;
    }

    public String toString() {
        return id + "\t" + String.format("%-8s", name) + "\t" + price + "\t" + remainingSeats;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getRemainingSeats() {
        return remainingSeats;
    }
}

    
