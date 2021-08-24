package com.redgrapefruit.itemnbt.itemnbt;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public interface ItemData {
    void readNbt(@NotNull Item item, @NotNull ItemStack stack, @NotNull NbtCompound nbt);
    void writeNbt(@NotNull Item item, @NotNull ItemStack stack, @NotNull NbtCompound nbt);
}
