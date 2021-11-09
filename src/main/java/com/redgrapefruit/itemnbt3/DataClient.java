package com.redgrapefruit.itemnbt3;

import com.redgrapefruit.itemnbt3.specification.DataCompound;
import com.redgrapefruit.itemnbt3.specification.Specification;
import com.redgrapefruit.itemnbt3.specification.linking.DataLink;
import com.redgrapefruit.itemnbt3.util.NbtCompoundMixinAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DataClient {
    private DataClient() {
        throw new RuntimeException("DataClient is not meant to be instantiated");
    }

    private static <T extends CustomData> void prepare(@NotNull T instance, @NotNull ItemStack stack) {
        Objects.requireNonNull(instance);
        Objects.requireNonNull(stack);

        final NbtCompound nbt = stack.getOrCreateSubNbt(instance.getNbtCategory());

        // If the data has not been initialized yet, write in the default data
        if (nbt.isEmpty()) {
            ((NbtCompoundMixinAccess) nbt).clearNbt();
            instance.writeNbt(nbt);
        }

        instance.readNbt(nbt);
    }

    public static <T extends CustomData> void use(@NotNull Supplier<T> factory, @NotNull ItemStack stack, @NotNull Consumer<T> action) {
        Objects.requireNonNull(factory);
        Objects.requireNonNull(stack);
        Objects.requireNonNull(action);

        // Use
        final T instance = factory.get();
        prepare(instance, stack);
        action.accept(instance);
        // Sync
        final NbtCompound nbt = stack.getOrCreateSubNbt(instance.getNbtCategory());
        ((NbtCompoundMixinAccess) nbt).clearNbt();
        instance.writeNbt(nbt);
    }

    public static void use(@NotNull ItemStack stack, @NotNull Specification specification, @NotNull Consumer<DataCompound> action) {
        Objects.requireNonNull(stack);
        Objects.requireNonNull(specification);
        Objects.requireNonNull(action);

        final NbtCompound subNbt = stack.getOrCreateSubNbt(specification.getId());
        final DataCompound compound = new DataCompound();

        if (subNbt.isEmpty()) {
            ((NbtCompoundMixinAccess) subNbt).clearNbt();
            specification.writeNbt(subNbt, compound);
        }

        specification.readNbt(subNbt, compound);
        action.accept(compound);
        ((NbtCompoundMixinAccess) subNbt).clearNbt();
        specification.writeNbt(subNbt, compound);
    }

    public static <T> void use(@NotNull ItemStack stack, @NotNull Specification specification, @NotNull DataLink link, @NotNull T instance, @NotNull Consumer<T> action) {
        Objects.requireNonNull(stack);
        Objects.requireNonNull(specification);
        Objects.requireNonNull(link);
        Objects.requireNonNull(action);

        final NbtCompound subNbt = stack.getOrCreateSubNbt(specification.getId());
        final DataCompound compound = new DataCompound();

        if (subNbt.isEmpty()) {
            ((NbtCompoundMixinAccess) subNbt).clearNbt();
            specification.writeNbt(subNbt, compound);
        }

        specification.readNbt(subNbt, compound);
        link.forwardLink(compound, instance);
        action.accept(instance);
        link.backwardLink(compound, instance);
        ((NbtCompoundMixinAccess) subNbt).clearNbt();
        specification.writeNbt(subNbt, compound);
    }
}
