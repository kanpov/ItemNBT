package com.redgrapefruit.itemnbt.itemnbt;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;

@FunctionalInterface
public interface ItemType {
    boolean check(@NotNull ItemStack stack);

    // <---- BASIC ITEMTYPES ---->

    /**
     * Creates a simple {@link ItemType} that checks if the given {@link ItemStack} is equal to the target {@link Item}
     *
     * @param target Target to check against
     * @return Constructed {@link ItemType}
     */
    static @NotNull ItemType equality(@NotNull Item target) {
        Objects.requireNonNull(target, "Target must not be null");

        return (stack) -> stack.getItem().equals(target);
    }

    /**
     * Creates a simple {@link ItemType} that checks the if the {@link ItemStack}'s {@link Item} is <code>instanceof</code> the given class
     *
     * @param targetClazz Target class
     * @return Constructed {@link ItemType}
     */
    static @NotNull ItemType instance(@NotNull Class<? extends ItemConvertible> targetClazz) {
        Objects.requireNonNull(targetClazz, "Target class must not be null");

        return (stack) -> targetClazz.isInstance(stack.getItem());
    }
}
