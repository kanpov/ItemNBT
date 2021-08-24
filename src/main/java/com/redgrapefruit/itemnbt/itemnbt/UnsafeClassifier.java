package com.redgrapefruit.itemnbt.itemnbt;

import net.minecraft.item.ItemStack;

/**
 * {@link UnsafeClassifier} is an annotation marking a {@link Classifier} which <i>could</i>
 * return <b>multiple results</b> for a single stack.
 * <br><br>
 * When using {@link Classifier}s marked with this annotation, {@link ItemDataManager#get(ItemStack, Classifier)}
 * is <b>unsafe</b> and should be <i>replaced</i> by a safe operation, like {@link ItemDataManager#getAll(ItemStack)}.
 */
public @interface UnsafeClassifier {}
