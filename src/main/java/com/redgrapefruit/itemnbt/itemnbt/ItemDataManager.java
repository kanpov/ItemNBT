package com.redgrapefruit.itemnbt.itemnbt;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.redgrapefruit.itemnbt.itemnbt.access.ItemStackMixinAccess;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * The {@link ItemDataManager} provides registering and search operations for your {@link ItemData} instances.
 */
@SuppressWarnings("ConstantConditions")
public final class ItemDataManager {
    /**
     * The internal registry map containing the {@link Classifier} and the factories ({@link Supplier}s) for your {@link ItemData}.
     *
     * To add into here, use the {@link #register(Classifier, Supplier)} method.
     */
    private static final @NotNull Map<Classifier, Supplier<ItemData>> classifierToFactoryMap = new HashMap<>();

    private ItemDataManager() {
        throw new UnsupportedOperationException("ItemDataManager must not be instantiated");
    }

    /**
     * Registers a factory for {@link ItemData} linked to a {@link Classifier}.<br>
     * For each {@link ItemData} class, this method must be called with the corresponding {@link Classifier}.
     *
     * @param classifier The {@link Classifier} to register the factory under
     * @param factory The factory that creates your {@link ItemData}. Typically, this should be a constructor reference
     */
    public static void register(@NotNull Classifier classifier, @NotNull Supplier<ItemData> factory) {
        Objects.requireNonNull(classifier, "Classifier is null");
        Objects.requireNonNull(factory, "Supplier<ItemData> is null");

        classifierToFactoryMap.putIfAbsent(classifier, factory);
    }

    /**
     * A search operation that returns <b>all</b> {@link ItemData} instances linked to the given {@link ItemStack}.
     *
     * @param stack The {@link ItemStack} to scan
     * @return The {@link ImmutableList} of all results. Can be empty
     */
    public static @NotNull ImmutableList<ItemData> getAll(@NotNull ItemStack stack) {
        Objects.requireNonNull(stack, "ItemStack is null");

        return ((ItemStackMixinAccess)(Object) stack).itemnbt$getAll();
    }

    /**
     * A search operation that returns <i>the first</i> {@link ItemData} instance linked to the given {@link ItemStack}
     * that matches the given {@link Classifier}. <b>The result is nullable</b>.
     * <br><br>
     * Typically, this approach is preferred to using {@link #getAll(ItemStack)}, but it <b>must not be used</b> for
     * {@link Classifier}s marked with the {@link UnsafeClassifier} annotation.
     *
     * @param stack The {@link ItemStack} to scan
     * @param targetClassifier The conditional {@link Classifier} that the result must match
     * @return The nullable search result
     */
    public static @Nullable ItemData get(@NotNull ItemStack stack, @NotNull Classifier targetClassifier) {
        Objects.requireNonNull(stack, "ItemStack is null");
        Objects.requireNonNull(targetClassifier, "Classifier is null");

        final ImmutableMap<Classifier, ItemData> allResults = ((ItemStackMixinAccess)(Object) stack).itemnbt$getAllWithContext();

        for (Classifier classifier : allResults.keySet()) {
            if (classifier.equals(targetClassifier)) {
                return allResults.get(classifier);
            }
        }

        return null;
    }

    /**
     * <b>Internal function. Must not be used</b>
     * <br><br>
     * Operates on matching instances.
     *
     * @param action The {@link BiConsumer} to be run
     * @param stack The {@link ItemStack} to scan
     */
    @ApiStatus.Internal
    public static void forAllMatching(@NotNull BiConsumer<Classifier, Supplier<ItemData>> action, @NotNull ItemStack stack) {
        classifierToFactoryMap.forEach((classifier, factory) -> {
            if (classifier.compute(stack)) {
                action.accept(classifier, factory);
            }
        });
    }
}
