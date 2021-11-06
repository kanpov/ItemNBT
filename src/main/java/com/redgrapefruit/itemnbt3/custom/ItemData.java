package com.redgrapefruit.itemnbt3.custom;

import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ItemData} provides you with the means of serializing data within your item.
 * <br><br>
 * Check out usage examples <a href="https://redgrapefruit.gitbook.io/itemnbt/api-examples/itemdata">here</a>
 */
public interface ItemData {
    /**
     * @return The name of the category, within which all of your data will be contained.
     */
    @NotNull String getNbtCategory();

    /**
     * Read the NBT data from disk into the state of your object.
     *
     * @param nbt The {@link NbtCompound} with data
     */
    void readNbt(@NotNull NbtCompound nbt);

    /**
     * Write the state of your object into the NBT data, which will be put to disk.
     *
     * @param nbt The {@link NbtCompound} for data
     */
    void writeNbt(@NotNull NbtCompound nbt);
}
