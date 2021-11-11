package com.redgrapefruit.itemnbt3.specification;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * A {@link DataCompound} is the result of serialization via a {@link Specification}.
 * <br><br>
 * It is a very simple wrapper around a map for normal data and another map for nested {@link DataCompound}s.
 */
public final class DataCompound {
    private final @NotNull Map<String, Object> rootTree = new HashMap<>();
    private final @NotNull Map<String, DataCompound> nestedTree = new HashMap<>();

    private void putRaw(@NotNull String key, @NotNull Object value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        rootTree.put(key, value);
    }

    /**
     * Put new data to the compound's root tree.
     *
     * @param key Data key.
     * @param value Data value.
     * @param <T> Type of that data.
     */
    public <T> void put(@NotNull String key, @NotNull T value) {
        putRaw(key, value);
    }

    /**
     * Put a nested compound into the nested tree.
     *
     * @param key Data key.
     * @param compound Nested compound.
     */
    public void putCompound(@NotNull String key, @NotNull DataCompound compound) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(compound);

        nestedTree.put(key, compound);
    }

    private @NotNull Object getRaw(@NotNull String key) {
        Objects.requireNonNull(key);

        return rootTree.get(key);
    }

    public <T> T get(@NotNull String key) {
        return (T) getRaw(key);
    }

    public @NotNull DataCompound getCompound(@NotNull String key) {
        Objects.requireNonNull(key);

        return nestedTree.get(key);
    }

    public @NotNull DataCompound getOrCreateCompound(@NotNull String key) {
        Objects.requireNonNull(key);

        if (nestedTree.containsKey(key)) {
            return nestedTree.get(key);
        } else {
            final DataCompound subCompound = new DataCompound();
            putCompound(key, subCompound);
            return subCompound;
        }
    }

    // SHORTCUTS

    public byte getByte(@NotNull String key) {
        return (byte) getRaw(key);
    }

    public short getShort(@NotNull String key) {
        return (short) getRaw(key);
    }

    public int getInt(@NotNull String key) {
        return (int) getRaw(key);
    }

    public long getLong(@NotNull String key) {
        return (long) getRaw(key);
    }

    public UUID getUUID(@NotNull String key) {
        return (UUID) getRaw(key);
    }

    public float getFloat(@NotNull String key) {
        return (float) getRaw(key);
    }

    public double getDouble(@NotNull String key) {
        return (double) getRaw(key);
    }

    public String getString(@NotNull String key) {
        return (String) getRaw(key);
    }

    public byte[] getByteArray(@NotNull String key) {
        return (byte[]) getRaw(key);
    }

    public int[] getIntArray(@NotNull String key) {
        return (int[]) getRaw(key);
    }

    public long[] getLongArray(@NotNull String key) {
        return (long[]) getRaw(key);
    }

    public boolean getBool(@NotNull String key) {
        return (boolean) getRaw(key);
    }
}
