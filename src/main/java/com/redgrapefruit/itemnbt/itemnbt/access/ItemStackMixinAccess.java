package com.redgrapefruit.itemnbt.itemnbt.access;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.redgrapefruit.itemnbt.itemnbt.Classifier;
import com.redgrapefruit.itemnbt.itemnbt.ItemData;
import org.jetbrains.annotations.NotNull;

public interface ItemStackMixinAccess {
    @NotNull ImmutableList<ItemData> itemnbt$retrieve();
    @NotNull ImmutableMap<Classifier, ItemData> itemnbt$retrieveWithContext();
}
