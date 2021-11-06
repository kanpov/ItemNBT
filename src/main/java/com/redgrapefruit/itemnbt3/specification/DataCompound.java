package com.redgrapefruit.itemnbt3.specification;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataCompound {
    private final @NotNull Map<String, Object> rootTree = new HashMap<>();
    private final @NotNull Map<String, DataCompound> nestedTree = new HashMap<>();

    private void putRaw(@NotNull String key, @NotNull Object value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        rootTree.put(key, value);
    }

    public <T> void put(@NotNull String key, @NotNull T value) {
        putRaw(key, value);
    }

    public void putCompound(@NotNull String key, @NotNull DataCompound compound) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(compound);

        nestedTree.put(key, compound);
    }

    private Object getRaw(@NotNull String key) {
        Objects.requireNonNull(key);

        return rootTree.get(key);
    }

    public <T> T get(@NotNull String key) {
        return (T) getRaw(key);
    }

    public DataCompound getCompound(@NotNull String key) {
        Objects.requireNonNull(key);

        return nestedTree.get(key);
    }
}
