package com.redgrapefruit.itemnbt3.specification;

import com.redgrapefruit.itemnbt3.util.Utilities;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Specification {
    private final @NotNull Map<String, TypeSerializer<?>> rootTree = new HashMap<>();
    private final @NotNull Map<String, Specification> nestedTree = new HashMap<>();
    private final @NotNull String id;

    public Specification(@NotNull String id) {
        Objects.requireNonNull(id);

        this.id = id;
    }

    public void add(@NotNull String key, @NotNull TypeSerializer<?> serializer) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(serializer);

        rootTree.putIfAbsent(key, serializer);
    }

    public void add(@NotNull String key, @NotNull Specification specification) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(specification);

        nestedTree.putIfAbsent(key, specification);
    }

    @ApiStatus.Internal
    public void writeNbt(@NotNull NbtCompound nbt, @NotNull DataCompound compound) {
        Objects.requireNonNull(nbt);
        Objects.requireNonNull(compound);

        // Write root tree
        rootTree.forEach((key, serializer) -> {
            serializer.writeNbt(key, nbt, compound.get(key));
        });

        // Write nested tree
        nestedTree.forEach((key, spec) -> {
            final NbtCompound subNbt = Utilities.getOrCreateSubNbt(nbt, key);
            spec.writeNbt(subNbt, compound.getOrCreateCompound(key));
        });
    }

    @ApiStatus.Internal
    public void readNbt(@NotNull NbtCompound nbt, @NotNull DataCompound compound) {
        Objects.requireNonNull(nbt);
        Objects.requireNonNull(compound);

        // Read root tree
        rootTree.forEach((key, serializer) -> {
            final Object value = serializer.readNbt(key, nbt);
            compound.put(key, value);
        });

        // Read nested tree
        nestedTree.forEach((key, spec) -> {
            final NbtCompound subNbt = Utilities.getOrCreateSubNbt(nbt, key);
            spec.readNbt(subNbt, compound.getOrCreateCompound(key));
        });
    }

    @ApiStatus.Internal
    public @NotNull String getId() {
        return id;
    }

    public static @NotNull Builder builder(@NotNull String id) {
        return new Builder(id);
    }

    public static class Builder {
        private final @NotNull Map<String, TypeSerializer<?>> rootTree = new HashMap<>();
        private final @NotNull Map<String, Specification> nestedTree = new HashMap<>();
        private final @NotNull String id;

        public Builder(@NotNull String id) {
            Objects.requireNonNull(id);

            this.id = id;
        }

        public @NotNull Builder add(@NotNull String key, @NotNull TypeSerializer<?> serializer) {
            Objects.requireNonNull(key);
            Objects.requireNonNull(serializer);

            rootTree.putIfAbsent(key, serializer);

            return this;
        }

        public @NotNull Builder add(@NotNull String key, @NotNull Specification specification) {
            Objects.requireNonNull(key);
            Objects.requireNonNull(specification);

            nestedTree.putIfAbsent(key, specification);

            return this;
        }

        public @NotNull Builder addByte(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.BYTE);
        }

        public @NotNull Builder addShort(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.SHORT);
        }

        public @NotNull Builder addInt(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.INT);
        }

        public @NotNull Builder addLong(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.LONG);
        }

        public @NotNull Builder addUUID(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.UUID);
        }

        public @NotNull Builder addFloat(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.FLOAT);
        }

        public @NotNull Builder addDouble(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.DOUBLE);
        }

        public @NotNull Builder addString(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.STRING);
        }

        public @NotNull Builder addByteArray(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.BYTE_ARRAY);
        }

        public @NotNull Builder addIntArray(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.INT_ARRAY);
        }

        public @NotNull Builder addLongArray(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.LONG_ARRAY);
        }

        public @NotNull Builder addBool(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.BOOL);
        }

        public @NotNull Specification build() {
            final Specification spec = new Specification(id);

            rootTree.forEach(spec::add);
            nestedTree.forEach(spec::add);

            return spec;
        }
    }
}
