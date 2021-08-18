package com.redgrapefruit.itemnbt.itemnbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Serializer} to save your NBT data from the given {@link NbtCompound}.
 */
@FunctionalInterface
public interface Serializer {
    void serialize(@NotNull NbtCompound nbt, @NotNull ItemStack stack);
}
