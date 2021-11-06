package com.redgrapefruit.itemnbt3.specification;

import net.minecraft.nbt.NbtCompound;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;

public class MappedTypeSerializer<T> implements TypeSerializer<T> {
    private final @NotNull TriConsumer<NbtCompound, String, T> writer;
    private final @NotNull BiFunction<NbtCompound, String, T> reader;
    private final @NotNull T fallback;

    public MappedTypeSerializer(@NotNull TriConsumer<NbtCompound, String, T> writer, @NotNull BiFunction<NbtCompound, String, T> reader, @NotNull T fallback) {
        Objects.requireNonNull(writer);
        Objects.requireNonNull(reader);
        Objects.requireNonNull(fallback);

        this.writer = writer;
        this.reader = reader;
        this.fallback = fallback;
    }

    @Override
    public T readNbt(@NotNull String key, @NotNull NbtCompound nbt) {
        return reader.apply(nbt, key);
    }

    @Override
    public void writeNbt(@NotNull String key, @NotNull NbtCompound nbt, @NotNull T value) {
        //noinspection ConstantConditions
        writer.accept(nbt, key, value == null ? fallback : value);
    }

    public static final @NotNull MappedTypeSerializer<Byte> BYTE = new MappedTypeSerializer<>(NbtCompound::putByte, NbtCompound::getByte, (byte)0);
    public static final @NotNull MappedTypeSerializer<Short> SHORT = new MappedTypeSerializer<>(NbtCompound::putShort, NbtCompound::getShort, (short)0);
    public static final @NotNull MappedTypeSerializer<Integer> INT = new MappedTypeSerializer<>(NbtCompound::putInt, NbtCompound::getInt, 0);
    public static final @NotNull MappedTypeSerializer<Long> LONG = new MappedTypeSerializer<>(NbtCompound::putLong, NbtCompound::getLong, 0L);
    public static final @NotNull MappedTypeSerializer<UUID> UUID = new MappedTypeSerializer<>(NbtCompound::putUuid, NbtCompound::getUuid, java.util.UUID.randomUUID());
    public static final @NotNull MappedTypeSerializer<Float> FLOAT = new MappedTypeSerializer<>(NbtCompound::putFloat, NbtCompound::getFloat, 0f);
    public static final @NotNull MappedTypeSerializer<Double> DOUBLE = new MappedTypeSerializer<>(NbtCompound::putDouble, NbtCompound::getDouble, 0.0);
    public static final @NotNull MappedTypeSerializer<String> STRING = new MappedTypeSerializer<>(NbtCompound::putString, NbtCompound::getString, "");
    public static final @NotNull MappedTypeSerializer<byte[]> BYTE_ARRAY = new MappedTypeSerializer<>(NbtCompound::putByteArray, NbtCompound::getByteArray, new byte[0]);
    public static final @NotNull MappedTypeSerializer<int[]> INT_ARRAY = new MappedTypeSerializer<>(NbtCompound::putIntArray, NbtCompound::getIntArray, new int[0]);
    public static final @NotNull MappedTypeSerializer<long[]> LONG_ARRAY = new MappedTypeSerializer<>(NbtCompound::putLongArray, NbtCompound::getLongArray, new long[0]);
    public static final @NotNull MappedTypeSerializer<Boolean> BOOL = new MappedTypeSerializer<>(NbtCompound::putBoolean, NbtCompound::getBoolean, false);
}
