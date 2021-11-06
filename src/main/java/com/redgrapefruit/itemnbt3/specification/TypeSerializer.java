package com.redgrapefruit.itemnbt3.specification;

import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public interface TypeSerializer<T> {
    T readNbt(@NotNull String key, @NotNull NbtCompound nbt);

    void writeNbt(@NotNull String key, @NotNull NbtCompound nbt, @NotNull T value);
}
