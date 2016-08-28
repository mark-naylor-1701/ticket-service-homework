// author: Mark W. Naylor
// file:   TicketService.java
// date:   2016-Aug-28

package com.walmart.homework;

import java.util.Optional;

public interface TicketService {
    int numSeatsAvailable(Optional<Integer> venueLevel);
}
