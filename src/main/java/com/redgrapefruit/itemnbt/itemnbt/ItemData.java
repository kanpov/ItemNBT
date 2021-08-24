package com.redgrapefruit.itemnbt.itemnbt;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ItemData} is the interface that is:
 *
 * <ul>
 *     <li>Attached to an {@link ItemStack} and synchronized with it</li>
 *     <li>Contains all persistent data for your {@link Item}</li>
 *     <li>Reads the data in {@link ItemData#readNbt(Item, ItemStack, NbtCompound)} from the {@link ItemStack}'s NBT</li>
 *     <li>Writes the data in {@link ItemData#writeNbt(Item, ItemStack, NbtCompound)} into the {@link ItemStack}'s NBT</li>
 * </ul>
 *
 * When you need to actually <i>use the data</i> from your {@link Item}, you need to retrieve the {@link ItemData}
 * instance using {@link ItemDataManager#get(ItemStack, Classifier)} or {@link ItemDataManager#getAll(ItemStack)}.
 * <br><br>
 * <b>Warning: </b> if the {@link Classifier} you registered your {@link ItemData} under is marked with the {@link UnsafeClassifier}
 * annotation, the {@link ItemDataManager#get(ItemStack, Classifier)} search is <b>unsafe</b> and <b>must not be used</b>.<br>
 * See {@link UnsafeClassifier}'s javadoc for more information.
 */
public interface ItemData {
    /**
     * Reads the NBT data from the {@link ItemStack}'s NBT into the instance.
     *
     * @param item The {@link Item} from the {@link ItemStack} to which the {@link ItemData} instance is currently attached
     * @param stack The {@link ItemStack} to which the {@link ItemData} instance is currently attached
     * @param nbt The {@link NbtCompound} to read your data from
     */
    void readNbt(@NotNull Item item, @NotNull ItemStack stack, @NotNull NbtCompound nbt);

    /**
     * Writes the NBT data into the {@link ItemStack}'s NBT from the instance.
     *
     * @param item The {@link Item} from the {@link ItemStack} to which the {@link ItemData} instance is currently attached
     * @param stack The {@link ItemStack} to which the {@link ItemData} instance is currently attached
     * @param nbt The {@link NbtCompound} to write your data into
     */
    void writeNbt(@NotNull Item item, @NotNull ItemStack stack, @NotNull NbtCompound nbt);
}
