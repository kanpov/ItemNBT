package com.redgrapefruit.itemnbt.itemnbt;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * A {@link Classifier} is an expression defining whether to apply certain saving & loading code to an {@link ItemStack}.<br>
 * Functionally, it's very similar to a Java {@link Predicate}.
 * <br><br>
 * Typically, you don't need a custom implementation of a {@link Classifier} (even though implementing it is fine),
 * so using the static factories is preferred.
 */
public interface Classifier {
    /**
     * Performs the computation.
     *
     * @param stack The {@link ItemStack} that the saving & loading code will be applied to in case of success
     * @return The result of the computation. True => proceed, false => ignore.
     */
    boolean compute(@NotNull ItemStack stack);

    /**
     * Creates a {@link Classifier} that checks if the {@link Item} in the {@link ItemStack} is instanceof the given class
     *
     * @param target The target {@link Class} to check against
     * @return The produced {@link Classifier}
     */
    static @NotNull Classifier ofType(Class<?> target) {
        Objects.requireNonNull(target, "Class<?> is null");

        return (stack) -> target.isInstance(stack.getItem());
    }

    /**
     * Creates a {@link Classifier} that checks if the {@link Item} in the {@link ItemStack} is instanceof <i>any</i> of the given classes.<br>
     * These {@link Classifier}s are unsafe for single-search operations, see {@link UnsafeClassifier} for more.
     *
     * @param targets An array of {@link Class}es to check against
     * @return The produced {@link Classifier}
     */
    @UnsafeClassifier
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

    /**
     * Creates a {@link Classifier} that checks if the {@link Item} in the {@link ItemStack} is equal to the target {@link Item}.
     *
     * @param target The target {@link Item} to check against
     * @return The produced {@link Classifier}
     */
    static @NotNull Classifier ofItem(Item target) {
        Objects.requireNonNull(target, "Item is null");

        return (stack) -> stack.getItem() == target;
    }

    /**
     * Creates a {@link Classifier} that checks if the {@link Item} in the {@link ItemStack} is equal to <i>any</i> of the target {@link Item}s.<br>
     * These {@link Classifier}s are unsafe for single-search operations, see {@link UnsafeClassifier} for more.
     *
     * @param targets An array of target {@link Item}s to check against.
     * @return The produced {@link Classifier}.
     */
    @UnsafeClassifier
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
