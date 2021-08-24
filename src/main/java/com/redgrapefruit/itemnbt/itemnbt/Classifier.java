package com.redgrapefruit.itemnbt.itemnbt;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public interface Classifier {
    boolean compute(@NotNull ItemStack stack);

    static @NotNull Classifier ofType(Class<?> target) {
        Objects.requireNonNull(target, "Class<?> is null");

        return (stack) -> target.isInstance(stack.getItem());
    }

    static @NotNull Classifier ofTypes(Class<?>... targets) {
        Objects.requireNonNull(targets, "Class<?>... is null");

        return (stack) -> {
            for (Class<?> target : targets) {
                if (target.isInstance(stack.getItem())) {
                    return true;
                }
            }
            return false;
        };
    }

    static @NotNull Classifier ofItem(Item target) {
        Objects.requireNonNull(target, "Item is null");

        return (stack) -> stack.getItem() == target;
    }

    static @NotNull Classifier ofItems(Item... targets) {
        Objects.requireNonNull(targets, "Item... is null");

        return (stack) -> {
            for (Item target : targets) {
                if (target == stack.getItem()) {
                    return true;
                }
            }
            return false;
        };
    }
}
