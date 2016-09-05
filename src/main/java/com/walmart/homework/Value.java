// author: Mark W. Naylor
// file:   Value.java
// date:   2016-Aug-27

package com.walmart.homework;

/**
 * An object from which a hidden value can be requested.
 */
interface Value<T> {
    /**
     * @return An appropriate form of the hidden value.
     */
    public T value();
}
