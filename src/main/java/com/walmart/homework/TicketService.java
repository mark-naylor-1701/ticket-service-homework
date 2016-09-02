// author: Mark W. Naylor
// file:   TicketService.java
// date:   2016-Aug-28

package com.walmart.homework;

import java.util.Optional;

public interface TicketService {
    int numSeatsAvailable(Optional<Integer> venueLevel);
    Optional<SeatHold> findAndHoldSeats(int numSeats,
                              Optional<Integer> minLevel,
                              Optional<Integer> maxLevel,
                              String customerEmail);
    String reserveSeats(int seatHoldId, String customerEmail);
}
