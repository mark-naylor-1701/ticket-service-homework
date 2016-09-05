// author: Mark W. Naylor
// file:   LevelName.java
// date:   2016-Aug-28

package com.walmart.homework;

/**
 * Wraps a String to create an explicit LevelName.
 */

public class LevelName extends TypeExtender<String> {
    /**
     * Constructs a new LevelName with the specified name.
     * @param name the value of the level name
     */
    public LevelName (String name) {
        super(name);
    }
}
