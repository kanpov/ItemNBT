package com.redgrapefruit.itemnbt3.specification;

import com.redgrapefruit.itemnbt3.util.NbtCompoundMixinAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public class DataClient {
    private DataClient() {
        throw new RuntimeException("DataClient is not meant to be instantiated.");
    }

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

    public static void synchronize(@NotNull ItemStack stack, @NotNull Specification specification, @NotNull DataCompound compound) {
        Objects.requireNonNull(stack);
        Objects.requireNonNull(specification);
        Objects.requireNonNull(compound);

        final NbtCompound subNbt = stack.getOrCreateSubNbt(specification.getId());
        ((NbtCompoundMixinAccess) subNbt).clearNbt();
        specification.writeNbt(subNbt, compound);
    }

    public static void use(@NotNull ItemStack stack, @NotNull Specification specification, @NotNull DataCompound compound, @NotNull Consumer<DataCompound> usage) {
        Objects.requireNonNull(stack);
        Objects.requireNonNull(specification);
        Objects.requireNonNull(compound);
        Objects.requireNonNull(usage);

        usage.accept(compound);
        synchronize(stack, specification, compound);
    }
}
