// author: Mark W. Naylor
// file:   Defaults.java
// date:   2016-Aug-30

package com.walmart.homework;

/**
 * Provides common information to be shared among modules.
 */
public final class Defaults {
    /**
     * How long, in milliseconds, the seat hold lasts before automatic
     * expiration takes place.
     */
    public static final int SEATHOLD_LIFESPAN = 2 * 1000;  // milliseconds

    /**
     * No Defaults objects can be instantiated.
     */
    private Defaults () {}
}
