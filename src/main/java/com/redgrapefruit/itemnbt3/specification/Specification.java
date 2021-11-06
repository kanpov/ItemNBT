package com.redgrapefruit.itemnbt3.specification;

import com.redgrapefruit.itemnbt3.util.Utilities;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Specification {
    private final @NotNull Map<String, TypeSerializer<Object>> rootTree = new HashMap<>();
    private final @NotNull Map<String, Specification> nestedTree = new HashMap<>();

    public @NotNull Specification add(@NotNull String key, @NotNull TypeSerializer<Object> serializer) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(serializer);

        rootTree.putIfAbsent(key, serializer);

        return this;
    }

    public @NotNull Specification add(@NotNull String key, @NotNull Specification specification) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(specification);

        nestedTree.putIfAbsent(key, specification);

        return this;
    }

    public void writeNbt(@NotNull NbtCompound nbt, @NotNull DataCompound compound) {
        Objects.requireNonNull(nbt);
        Objects.requireNonNull(compound);

        // Write root tree
        rootTree.forEach((key, serializer) -> {
            Object value = compound.get(key);
            serializer.writeNbt(key, nbt, value);
        });

        // Write nested tree
        nestedTree.forEach((key, spec) -> {
            final NbtCompound subNbt = Utilities.getOrCreateSubNbt(nbt, key);
            spec.writeNbt(subNbt, compound.getCompound(key));
        });
    }

    public void readNbt(@NotNull NbtCompound nbt, @NotNull DataCompound compound) {
        Objects.requireNonNull(nbt);
        Objects.requireNonNull(compound);

        // Read root tree
        rootTree.forEach((key, serializer) -> {
            Object value = serializer.readNbt(key, nbt);
            compound.put(key, value);
        });

        // Read nested tree
        nestedTree.forEach((key, spec) -> {
            final NbtCompound subNbt = Utilities.getOrCreateSubNbt(nbt, key);
            spec.readNbt(subNbt, compound.getCompound(key));
        });
    }
}
