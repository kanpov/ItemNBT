package com.redgrapefruit.itemnbt3.serializer;

import com.redgrapefruit.itemnbt3.field.FieldSerializer;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link TypeSerializer} provides you with the means of serializing the {@link T} data type into its
 * NBT representation and back.
 *
 * @param <T> The data type that this {@link TypeSerializer} handles
 * @deprecated The {@link TypeSerializer} system is deprecated in favor of the more advanced {@link FieldSerializer} system.
 */
@Deprecated
public interface TypeSerializer<T> {
    /**
     * Read your data type from its NBT representation.
     *
     * @param key The NBT key, under which the data type is stored.
     * @param nbt The {@link NbtCompound} with the data.
     * @return The value of the {@link T} data type.
     */
    T readNbt(@NotNull String key, @NotNull NbtCompound nbt);

    /**
     * Write your data type into its NBT representation.
     *
     * @param key The NBT key, under which the data type will be stored.
     * @param nbt The {@link NbtCompound}, to which you should write your data.
     * @param value The value of the {@link T} data type.
     */
    void writeNbt(@NotNull String key, @NotNull NbtCompound nbt, @NotNull T value);
}
