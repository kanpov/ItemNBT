package com.redgrapefruit.itemnbt3.specification;

import net.minecraft.nbt.NbtCompound;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;

public class BuiltinTypeSerializer<T> implements TypeSerializer<T> {
    private final @NotNull TriConsumer<NbtCompound, String, T> writer;
    private final @NotNull BiFunction<NbtCompound, String, T> reader;
    private final @NotNull T fallback;

    public BuiltinTypeSerializer(@NotNull TriConsumer<NbtCompound, String, T> writer, @NotNull BiFunction<NbtCompound, String, T> reader, @NotNull T fallback) {
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

    public static final @NotNull BuiltinTypeSerializer<Byte> BYTE = new BuiltinTypeSerializer<>(NbtCompound::putByte, NbtCompound::getByte, (byte)0);
    public static final @NotNull BuiltinTypeSerializer<Short> SHORT = new BuiltinTypeSerializer<>(NbtCompound::putShort, NbtCompound::getShort, (short)0);
    public static final @NotNull BuiltinTypeSerializer<Integer> INT = new BuiltinTypeSerializer<>(NbtCompound::putInt, NbtCompound::getInt, 0);
    public static final @NotNull BuiltinTypeSerializer<Long> LONG = new BuiltinTypeSerializer<>(NbtCompound::putLong, NbtCompound::getLong, 0L);
    public static final @NotNull BuiltinTypeSerializer<UUID> UUID = new BuiltinTypeSerializer<>(NbtCompound::putUuid, NbtCompound::getUuid, java.util.UUID.randomUUID());
    public static final @NotNull BuiltinTypeSerializer<Float> FLOAT = new BuiltinTypeSerializer<>(NbtCompound::putFloat, NbtCompound::getFloat, 0f);
    public static final @NotNull BuiltinTypeSerializer<Double> DOUBLE = new BuiltinTypeSerializer<>(NbtCompound::putDouble, NbtCompound::getDouble, 0.0);
    public static final @NotNull BuiltinTypeSerializer<String> STRING = new BuiltinTypeSerializer<>(NbtCompound::putString, NbtCompound::getString, "");
    public static final @NotNull BuiltinTypeSerializer<byte[]> BYTE_ARRAY = new BuiltinTypeSerializer<>(NbtCompound::putByteArray, NbtCompound::getByteArray, new byte[0]);
    public static final @NotNull BuiltinTypeSerializer<int[]> INT_ARRAY = new BuiltinTypeSerializer<>(NbtCompound::putIntArray, NbtCompound::getIntArray, new int[0]);
    public static final @NotNull BuiltinTypeSerializer<long[]> LONG_ARRAY = new BuiltinTypeSerializer<>(NbtCompound::putLongArray, NbtCompound::getLongArray, new long[0]);
    public static final @NotNull BuiltinTypeSerializer<Boolean> BOOL = new BuiltinTypeSerializer<>(NbtCompound::putBoolean, NbtCompound::getBoolean, false);
}
