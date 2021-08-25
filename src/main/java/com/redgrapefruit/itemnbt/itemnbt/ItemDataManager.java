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
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * The {@link ItemDataManager} provides registering and search operations for your {@link ItemData} instances.
 */
@SuppressWarnings("ConstantConditions")
public final class ItemDataManager {
    // region Register

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

    // endregion

    // region Search

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
     * Returns the found {@link ItemData} of the {@link T} type or throws a {@link ClassCastException} and/or {@link NullPointerException}.
     * <br><br>
     * <b>Warning:</b> this method is <b>forbidden</b> for use with unsafe {@link Classifier}s.
     *
     * @param stack The {@link ItemStack} to scan
     * @param classifier The {@link Classifier} to match when searching
     * @param <T> The {@link ItemData} type to cast to. The cast is unchecked, which means a {@link ClassCastException} might occur on failure
     * @return The resulting not-null {@link T} value.
     *
     * @since 2.1
     */
    @SuppressWarnings("unchecked")
    public static <T extends ItemData> @NotNull T getOrThrow(@NotNull ItemStack stack, @NotNull Classifier classifier) {
        final @Nullable ItemData result = get(stack, classifier);
        Objects.requireNonNull(result, "Search operation failed");
        return (T) result;
    }

    // endregion

    // region Compute

    /**
     * Performs a {@link Consumer} computation on all {@link ItemData} attached to an {@link ItemStack}.
     *
     * @param stack The {@link ItemStack} to scan
     * @param computation The {@link Consumer} computation to perform on every found result
     * @return The success of the operation. The operation fails if the {@link ImmutableList} of results is empty
     *
     * @since 2.1
     */
    public static boolean computeAll(@NotNull ItemStack stack, @NotNull Consumer<ItemData> computation) {
        Objects.requireNonNull(computation, "Consumer<ItemData> is null");

        final ImmutableList<ItemData> results = getAll(stack);
        if (results.isEmpty()) return false; // if no results have been found, the operation has failed
        results.forEach(computation);
        return true;
    }

    /**
     * Performs a {@link Consumer} computation on the first {@link ItemData} attached to an {@link ItemData}<br>
     * if it matches the given {@link Classifier}.
     * <br><br>
     * <b>Warning:</b> this method is <b>forbidden</b> for use with unsafe {@link Classifier}s
     *
     * @param stack The {@link ItemStack} to scan
     * @param classifier The {@link Classifier} that the resulting {@link ItemData} must match in order to proceed
     * @param computation The {@link Consumer} computation to perform on the resulting {@link ItemData}
     * @return The success of the operation. The operation fails if the result of the {@link #get} operation is null (nothing found)
     *
     * @since 2.1
     */
    public static boolean compute(@NotNull ItemStack stack, @NotNull Classifier classifier, @NotNull Consumer<ItemData> computation) {
        Objects.requireNonNull(computation, "Consumer<ItemData> is null");

        final @Nullable ItemData result = get(stack, classifier);
        if (result == null) return false; // if nothing's found, the operation has failed
        computation.accept(result);
        return true;
    }

    /**
     * Performs a {@link Consumer} computation on every {@link ItemData} instance attached to an {@link ItemStack}<br>
     * if the {@link Predicate} condition is met.
     *
     * @param stack The {@link ItemStack} to scan
     * @param condition The {@link Predicate} condition that the {@link ItemData} instance must match in order to proceed with the computation
     * @param computation The {@link Consumer} computation that will be executed on every matching instance
     *
     * @since 2.1
     */
    public static void computeAllIf(@NotNull ItemStack stack, @NotNull Predicate<ItemData> condition, @NotNull Consumer<ItemData> computation) {
        Objects.requireNonNull(condition, "Predicate<ItemData> is null");
        Objects.requireNonNull(computation, "Consumer<ItemData> is null");

        final ImmutableList<ItemData> results = getAll(stack);

        for (ItemData data : results) {
            if (condition.test(data)) {
                computation.accept(data);
            }
        }
    }

    /**
     * Performs a {@link Consumer} computation on the first {@link ItemData} instance. That instance must also match<br>
     * the {@link Predicate} condition.
     * <br><br>
     * <b>Warning:</b> this method is <b>forbidden</b> for use with unsafe {@link Classifier}s
     *
     * @param stack The {@link ItemStack} to scan
     * @param classifier The {@link Classifier} representing the first condition that must be met
     * @param condition The {@link Predicate} second condition that must also be met
     * @param computation The {@link Consumer} computation that will occur if both conditions are met
     *
     * @since 2.1
     */
    public static void computeIf(@NotNull ItemStack stack, @NotNull Classifier classifier, @NotNull Predicate<ItemData> condition, @NotNull Consumer<ItemData> computation) {
        Objects.requireNonNull(condition, "Predicate<ItemData> is null");
        Objects.requireNonNull(computation, "Consumer<ItemData> is null");

        final @Nullable ItemData result = get(stack, classifier);

        if (result != null) {
            if (condition.test(result)) {
                computation.accept(result);
            }
        }
    }

    /**
     * Performs a {@link Consumer} computation on all {@link ItemData} instances attached to an {@link ItemStack}<br>
     * or throws a {@link RuntimeException} indicating failure.
     *
     * @param stack The {@link ItemStack} to scan
     * @param computation The {@link Consumer} computation that will be executed in case of success
     */
    public static void computeAllOrThrow(@NotNull ItemStack stack, @NotNull Consumer<ItemData> computation) {
        if (!computeAll(stack, computation)) throw new RuntimeException("Computation failed. No results have been found");
    }

    /**
     * Performs a {@link Consumer} computation on the first {@link ItemData} instance attached to an {@link ItemStack}<br>
     * and matching the {@link Classifier} or throws a {@link RuntimeException} indicating failure.
     *
     * @param stack The {@link ItemStack} to scan
     * @param classifier The {@link Classifier} that the {@link ItemData} instance must match
     * @param computation The {@link Consumer} computation that will be executed in case of success
     */
    public static void computeOrThrow(@NotNull ItemStack stack, @NotNull Classifier classifier, @NotNull Consumer<ItemData> computation) {
        if (!compute(stack, classifier, computation)) throw new RuntimeException("Computation failed. No results have been found");
    }

    // endregion

    // region Internal

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

    // endregion
}
