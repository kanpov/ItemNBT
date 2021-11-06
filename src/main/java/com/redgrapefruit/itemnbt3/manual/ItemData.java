package com.redgrapefruit.itemnbt3.manual;

import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public interface ItemData {
    @NotNull String getNbtCategory();

    void readNbt(@NotNull NbtCompound nbt);

    void writeNbt(@NotNull NbtCompound nbt);
}
