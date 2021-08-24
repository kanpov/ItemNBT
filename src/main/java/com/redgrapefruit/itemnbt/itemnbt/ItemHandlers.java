package com.redgrapefruit.itemnbt.itemnbt;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.redgrapefruit.itemnbt.itemnbt.access.ItemStackMixinAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
public final class ItemHandlers {
    private static final @NotNull Map<Classifier, Supplier<ItemData>> classifierToFactoryMap = new HashMap<>();

    public static <T extends Item> void register(@NotNull Classifier classifier, @NotNull Supplier<ItemData> factory) {
        Objects.requireNonNull(classifier, "Classifier is null");
        Objects.requireNonNull(factory, "Supplier<ItemData> is null");

        classifierToFactoryMap.putIfAbsent(classifier, factory);
    }

    public static @NotNull ImmutableList<ItemData> getAll(@NotNull ItemStack stack) {
        Objects.requireNonNull(stack, "ItemStack is null");

        return ((ItemStackMixinAccess)(Object) stack).itemnbt$retrieve();
    }

    public static @Nullable ItemData get(@NotNull ItemStack stack, @NotNull Classifier targetClassifier) {
        Objects.requireNonNull(stack, "ItemStack is null");
        Objects.requireNonNull(targetClassifier, "Classifier is null");

        final ImmutableMap<Classifier, ItemData> allResults = ((ItemStackMixinAccess)(Object) stack).itemnbt$retrieveWithContext();

        for (Classifier classifier : allResults.keySet()) {
            if (classifier.equals(targetClassifier)) {
                return allResults.get(classifier);
            }
        }

        return null;
    }

    @ApiStatus.Internal
    public static void forAllMatching(@NotNull BiConsumer<Classifier, Supplier<ItemData>> action, @NotNull ItemStack stack) {
        classifierToFactoryMap.forEach((classifier, factory) -> {
            if (classifier.compute(stack)) {
                action.accept(classifier, factory);
            }
        });
    }
}
