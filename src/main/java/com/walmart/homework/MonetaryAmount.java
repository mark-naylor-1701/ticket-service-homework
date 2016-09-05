// author: Mark W. Naylor
// file:   MonetaryAmount.java
// date:   2016-Aug-28

package com.walmart.homework;

/**
 * Wraps a Double to explicitly describe an amount of money.
 *
 */
public class MonetaryAmount extends TypeExtender<Double> {
    /**
     * Constructs a new MonetaryAmount with the specified amount.
     * @param amount The amount of money, assumed to be USD.
     */
    public MonetaryAmount (Double amount) {
        super(amount);
    }
}
