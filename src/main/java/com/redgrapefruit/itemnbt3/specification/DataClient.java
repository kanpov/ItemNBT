package com.redgrapefruit.itemnbt3.specification;

import com.redgrapefruit.itemnbt3.util.NbtCompoundMixinAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A {@link DataClient} provides main operations for working with specification-based serialization.
 */
public class DataClient {
    private DataClient() {
        throw new RuntimeException("DataClient is not meant to be instantiated.");
    }

    /**
     * Reads and initializes data from an {@link ItemStack}'s NBT according to the given {@link Specification}.
     *
     * @param stack The {@link ItemStack}, whose NBT contains the necessary data.
     * @param specification The {@link Specification} for reading that data.
     * @return The output {@link DataCompound}.
     */
    public static @NotNull DataCompound get(@NotNull ItemStack stack, @NotNull Specification specification) {
        Objects.requireNonNull(stack);
        Objects.requireNonNull(specification);

        final NbtCompound subNbt = stack.getOrCreateSubNbt(specification.getId());
        final DataCompound compound = new DataCompound();

        if (subNbt.isEmpty()) {
            ((NbtCompoundMixinAccess) subNbt).clearNbt();
            specification.writeNbt(subNbt, compound);
        }

        specification.readNbt(subNbt, compound);

        return compound;
    }

    /**
     * Synchronizes all new changes in the given {@link DataCompound} into the given {@link ItemStack}'s NBT.<br>
     * Synchronization is <b>required</b> after any changes are made, or else they will be lost.
     *
     * @param stack The {@link ItemStack} to synchronize the changes into.
     * @param specification The {@link Specification}, according to which the data is managed.
     * @param compound The {@link DataCompound} with all made changes.
     */
    public static void synchronize(@NotNull ItemStack stack, @NotNull Specification specification, @NotNull DataCompound compound) {
        Objects.requireNonNull(stack);
        Objects.requireNonNull(specification);
        Objects.requireNonNull(compound);

        final NbtCompound subNbt = stack.getOrCreateSubNbt(specification.getId());
        ((NbtCompoundMixinAccess) subNbt).clearNbt();
        specification.writeNbt(subNbt, compound);
    }

    /**
     * Performs a computation on a {@link DataCompound} and synchronizes it with
     * {@link #synchronize(ItemStack, Specification, DataCompound)} right afterwards.
     *
     * @param stack The {@link ItemStack} to synchronize the changes into.
     * @param specification The {@link Specification}, according to which the data is managed.
     * @param compound The {@link DataCompound}, to which the computation will be applied.
     * @param usage The lambda computation, in which you can safely make changes without fear of data loss.
     */
    public static void use(@NotNull ItemStack stack, @NotNull Specification specification, @NotNull DataCompound compound, @NotNull Consumer<DataCompound> usage) {
        Objects.requireNonNull(stack);
        Objects.requireNonNull(specification);
        Objects.requireNonNull(compound);
        Objects.requireNonNull(usage);

        usage.accept(compound);
        synchronize(stack, specification, compound);
    }
}
