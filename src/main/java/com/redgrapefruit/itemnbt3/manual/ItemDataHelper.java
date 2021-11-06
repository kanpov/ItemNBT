package com.redgrapefruit.itemnbt3.manual;

import com.redgrapefruit.itemnbt3.util.NbtCompoundMixinAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemDataHelper {
    private ItemDataHelper() {
        throw new RuntimeException("ItemDataManager is not meant to be instantiated.");
    }

    private static <T extends ItemData> void prepare(@NotNull T instance, @NotNull ItemStack stack) {
        Objects.requireNonNull(instance);
        Objects.requireNonNull(stack);

        final NbtCompound nbt = stack.getOrCreateSubNbt(instance.getNbtCategory());

        if (nbt.isEmpty()) {
            ((NbtCompoundMixinAccess) nbt).clearNbt();
            instance.writeNbt(nbt);
        }

        instance.readNbt(nbt);
    }

    public static <T extends ItemData> T get(@NotNull Supplier<T> factory, @NotNull ItemStack stack) {
        Objects.requireNonNull(factory);
        Objects.requireNonNull(stack);

        final T instance = factory.get();
        prepare(instance, stack);
        return instance;
    }

    public static void synchronize(@NotNull ItemStack stack, @NotNull ItemData data) {
        Objects.requireNonNull(stack);
        Objects.requireNonNull(data);

        final NbtCompound nbt = stack.getOrCreateSubNbt(data.getNbtCategory());
        ((NbtCompoundMixinAccess) nbt).clearNbt();
        data.writeNbt(nbt);
    }

    public static <T extends ItemData> void use(@NotNull ItemStack stack, @NotNull T data, @NotNull Consumer<T> usage) {
        Objects.requireNonNull(stack);
        Objects.requireNonNull(data);
        Objects.requireNonNull(usage);

        usage.accept(data);
        synchronize(stack, data);
    }
}
