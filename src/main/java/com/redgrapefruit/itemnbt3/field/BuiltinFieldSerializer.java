package com.redgrapefruit.itemnbt3.field;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;

public class BuiltinFieldSerializer<T> implements FieldSerializer<T> {
    private final @NotNull TriConsumer<NbtCompound, String, T> writer;
    private final @NotNull BiFunction<NbtCompound, String, T> reader;
    private final @NotNull T fallback;
    private final @NotNull Identifier id;

    public BuiltinFieldSerializer(@NotNull TriConsumer<NbtCompound, String, T> writer, @NotNull BiFunction<NbtCompound, String, T> reader, @NotNull T fallback, @NotNull Identifier id) {
        Objects.requireNonNull(writer);
        Objects.requireNonNull(reader);
        Objects.requireNonNull(fallback);
        Objects.requireNonNull(id);

        this.writer = writer;
        this.reader = reader;
        this.fallback = fallback;
        this.id = id;
    }

    @Override
    public @NotNull T readDataFromNbt(@NotNull NbtCompound dedicatedNbt, @NotNull NbtCompound metadataNbt, @NotNull NbtCompound rootNbt) {
        return reader.apply(dedicatedNbt, "Value");
    }

    @Override
    public void writeDataToNbt(@NotNull NbtCompound dedicatedNbt, @NotNull NbtCompound metadataNbt, @NotNull NbtCompound rootNbt, @NotNull T value) {
        writer.accept(dedicatedNbt, "Value", value);
    }

    @Override
    public void writeDefaultValue(@NotNull NbtCompound dedicatedNbt, @NotNull NbtCompound metadataNbt, @NotNull NbtCompound rootNbt) {
        writer.accept(dedicatedNbt, "Value", fallback);
    }

    @Override
    public @NotNull Identifier getId() {
        return id;
    }

    public static final @NotNull BuiltinFieldSerializer<Byte> BYTE = new BuiltinFieldSerializer<>(
            NbtCompound::putByte, NbtCompound::getByte, (byte) 0, new Identifier("itemnbt", "S_byte"));

    public static final @NotNull BuiltinFieldSerializer<Short> SHORT = new BuiltinFieldSerializer<>(
            NbtCompound::putShort, NbtCompound::getShort, (short)0, new Identifier("itemnbt", "S_short"));

    public static final @NotNull BuiltinFieldSerializer<Integer> INT = new BuiltinFieldSerializer<>(
            NbtCompound::putInt, NbtCompound::getInt, 0, new Identifier("itemnbt", "S_int"));

    public static final @NotNull BuiltinFieldSerializer<Long> LONG = new BuiltinFieldSerializer<>(
            NbtCompound::putLong, NbtCompound::getLong, 0L, new Identifier("itemnbt", "S_long"));

    public static final @NotNull BuiltinFieldSerializer<UUID> UUID = new BuiltinFieldSerializer<>(
            NbtCompound::putUuid, NbtCompound::getUuid, java.util.UUID.randomUUID(), new Identifier("itemnbt", "S_UUID"));

    public static final @NotNull BuiltinFieldSerializer<Float> FLOAT = new BuiltinFieldSerializer<>(
            NbtCompound::putFloat, NbtCompound::getFloat, 0f, new Identifier("itemnbt", "S_float"));

    public static final @NotNull BuiltinFieldSerializer<Double> DOUBLE = new BuiltinFieldSerializer<>(
            NbtCompound::putDouble, NbtCompound::getDouble, 0.0, new Identifier("itemnbt", "S_double"));

    public static final @NotNull BuiltinFieldSerializer<String> STRING = new BuiltinFieldSerializer<>(
            NbtCompound::putString, NbtCompound::getString, "", new Identifier("itemnbt", "S_string"));

    public static final @NotNull BuiltinFieldSerializer<byte[]> BYTE_ARRAY = new BuiltinFieldSerializer<>(
            NbtCompound::putByteArray, NbtCompound::getByteArray, new byte[] {}, new Identifier("itemnbt", "S_byte_array"));

    public static final @NotNull BuiltinFieldSerializer<int[]> INT_ARRAY = new BuiltinFieldSerializer<>(
            NbtCompound::putIntArray, NbtCompound::getIntArray, new int[] {}, new Identifier("itemnbt", "S_int_array"));

    public static final @NotNull BuiltinFieldSerializer<long[]> LONG_ARRAY = new BuiltinFieldSerializer<>(
            NbtCompound::putLongArray, NbtCompound::getLongArray, new long[] {}, new Identifier("itemnbt", "S_long_array"));

    public static final @NotNull BuiltinFieldSerializer<Boolean> BOOL = new BuiltinFieldSerializer<>(
            NbtCompound::putBoolean, NbtCompound::getBoolean, false, new Identifier("itemnbt", "S_bool"));
}
