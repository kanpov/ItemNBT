package com.redgrapefruit.itemnbt.itemnbt;

import com.google.common.collect.ImmutableList;
import com.redgrapefruit.itemnbt.itemnbt.internal.ItemNBTEntry;
import com.redgrapefruit.itemnbt.itemnbt.mixin.ItemStackMixin;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The {@link ItemNBT} is the main API of this library.
 * <br><br>
 * It allows you to register {@link ItemNBTEntry} for your items, which are then picked up
 * by the {@link ItemStackMixin} and used.
 */
public final class ItemNBT {
    /**
     * This {@link List} is the internal registry which stores all entries.
     */
    private static final List<ItemNBTEntry> registry = new ArrayList<>();

    /**
     * This class must not be instantiated, since it's meant to be used in a static context.
     */
    private ItemNBT() {}

    /**
     * Registers a new entry to the system. <b>Must only be called in a static context</b>
     * <br><br>
     * Example:<br>
     * <code>
     * static {
     *     ItemNBT.register(
     *         (item) -> item instanceof MyItem,
     *         (nbt, stack) -> {
     *             // Your saving code
     *         },
     *         (nbt, stack) -> {
     *             // Your loading code
     *         }
     *     );
     * }
     * </code>
     *
     * @param type The {@link ItemType} to which the entry applies. Preferred to create using a lambda
     * @param serializer The {@link Serializer} to save your NBT data. Preferred to create using a lambda
     * @param deserializer The {@link Deserializer} to load your NBT data. Preferred to create using a lambda
     */
    public static void register(@NotNull ItemType type, @NotNull Serializer serializer, @NotNull Deserializer deserializer) {
        Objects.requireNonNull(type, "ItemType must not be null");
        Objects.requireNonNull(serializer, "Serializer must not be null");
        Objects.requireNonNull(deserializer, "Deserializer must not be null");

        registry.add(new ItemNBTEntry(type, serializer, deserializer));
    }

    /**
     * Searches for all {@link ItemNBTEntry}s, per which the given {@link ItemStack} is valid.
     *
     * @param stack The {@link ItemStack} to validate
     * @return An {@link ImmutableList} of all accumulated {@link ItemNBTEntry}s
     */
    @ApiStatus.Internal
    public static ImmutableList<ItemNBTEntry> search(@NotNull ItemStack stack) {
        Objects.requireNonNull(stack, "ItemStack must not be null");

        ImmutableList.Builder<ItemNBTEntry> builder = ImmutableList.builder();

        for (ItemNBTEntry entry : registry) {
            if (entry.getType().check(stack)) {
                builder.add(entry);
            }
        }

        return builder.build();
    }
}
