package com.redgrapefruit.itemnbt.itemnbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Deserializer} to load your NBT data from the given {@link NbtCompound}.
 * <br><br>
 * {@link Deserializer}s should be created using lambdas, like this: <code>(item, nbt) -> { ... }</code>
 */
@FunctionalInterface
public interface Deserializer {
    void deserialize(@NotNull NbtCompound nbt, @NotNull ItemStack stack);
}
