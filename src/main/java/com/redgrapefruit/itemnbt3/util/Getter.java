package com.redgrapefruit.itemnbt3.util;

@FunctionalInterface
public interface Getter<O, T> {
    T get(O instance);
}
