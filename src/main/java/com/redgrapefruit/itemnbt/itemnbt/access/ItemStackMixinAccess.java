package com.redgrapefruit.itemnbt.itemnbt.access;

import net.minecraft.item.ItemStack;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.redgrapefruit.itemnbt.itemnbt.Classifier;
import com.redgrapefruit.itemnbt.itemnbt.ItemData;
import com.redgrapefruit.itemnbt.itemnbt.ItemDataManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ItemStackMixinAccess} provides access to internal operations for locating {@link ItemData}
 * instances by {@link ItemStack}s they belong to.
 * <br><br>
 * <b>Warning</b>: This interface is internal and should not be used. Please use the corresponding
 * functions in {@link ItemDataManager}
 */
@ApiStatus.Internal
public interface ItemStackMixinAccess {
    /**
     * Retrieves all {@link ItemData} instances associated with the {@link ItemStack} in an {@link ImmutableList}.
     *
     * @return Produced {@link ImmutableList} of results.
     */
    @NotNull ImmutableList<ItemData> itemnbt$getAll();

    /**
     * Retrieves an {@link ImmutableMap} with {@link ItemData} and {@link Classifier}s they are attached to.<br>
     * Used for extra context when performing more advanced search.
     *
     * @return Produced {@link ImmutableMap} of results.
     */
    @NotNull ImmutableMap<Classifier, ItemData> itemnbt$getAllWithContext();
}
