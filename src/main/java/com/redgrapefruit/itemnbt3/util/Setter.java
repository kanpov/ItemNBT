package com.redgrapefruit.itemnbt3.util;

@FunctionalInterface
public interface Setter<O, T> {
    void set(O instance, T value);
}
