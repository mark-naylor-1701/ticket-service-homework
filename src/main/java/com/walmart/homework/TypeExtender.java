// author: Mark W. Naylor
// file:   TypeExtender.java
// date:   2016-Aug-27

package com.walmart.homework;

abstract public class TypeExtender<T> implements Valuable<T> {
    private T _value;

    public TypeExtender(T t) {
        _value = t;
    }

    public T value() {
        return _value;
    }
}
