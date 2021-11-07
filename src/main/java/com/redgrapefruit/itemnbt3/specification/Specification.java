package com.redgrapefruit.itemnbt3.specification;

import com.redgrapefruit.itemnbt3.custom.CustomData;
import com.redgrapefruit.itemnbt3.util.Utilities;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * A {@link Specification} is the core for specification-based serialization.
 * <br><br>
 * It serves as a template for reading and writing your NBT data by containing:
 * <ul>
 *     <li>Your <b>root tree</b> with a {@link TypeSerializer} for every field</li>
 *     <li>Your <b>nested tree</b> with nested {@link Specification}s, if you have more than one level of nesting</li>
 *     <li>Your <b>identifier</b>, which serves the same purpose as an NBT category in {@link CustomData}</li>
 * </ul>
 * The {@link Builder} is preferred for building out {@link Specification}s manually.
 */
public class Specification {
    private final @NotNull Map<String, TypeSerializer<?>> rootTree = new HashMap<>();
    private final @NotNull Map<String, Specification> nestedTree = new HashMap<>();
    private final @NotNull String id;

    public Specification(@NotNull String id) {
        Objects.requireNonNull(id);

        this.id = id;
    }

    /**
     * Add a new element to the {@link Specification}'s root tree.
     *
     * @param key The NBT key.
     * @param serializer The {@link TypeSerializer} to read & write that key.
     */
    public void add(@NotNull String key, @NotNull TypeSerializer<?> serializer) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(serializer);

        rootTree.putIfAbsent(key, serializer);
    }

    /**
     * Adds a new element to the {@link Specification}'s nested tree.
     *
     * @param key The NBT key.
     * @param specification The nested {@link Specification}.
     */
    public void add(@NotNull String key, @NotNull Specification specification) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(specification);

        nestedTree.putIfAbsent(key, specification);
    }

    @ApiStatus.Internal
    public void writeNbt(@NotNull NbtCompound nbt, @NotNull DataCompound compound) {
        Objects.requireNonNull(nbt);
        Objects.requireNonNull(compound);

        rootTree.forEach((key, serializer) -> {
            serializer.writeNbt(key, nbt, compound.get(key));
        });

        nestedTree.forEach((key, spec) -> {
            final NbtCompound subNbt = Utilities.getOrCreateSubNbt(nbt, key);
            spec.writeNbt(subNbt, compound.getOrCreateCompound(key));
        });
    }

    @ApiStatus.Internal
    public void readNbt(@NotNull NbtCompound nbt, @NotNull DataCompound compound) {
        Objects.requireNonNull(nbt);
        Objects.requireNonNull(compound);

        rootTree.forEach((key, serializer) -> {
            final Object value = serializer.readNbt(key, nbt);
            compound.put(key, value);
        });

        nestedTree.forEach((key, spec) -> {
            final NbtCompound subNbt = Utilities.getOrCreateSubNbt(nbt, key);
            spec.readNbt(subNbt, compound.getOrCreateCompound(key));
        });
    }

    @ApiStatus.Internal
    public @NotNull String getId() {
        return id;
    }

    /**
     * Creates a new instance of a {@link Builder} in a more convenient way.
     *
     * @param id The identifier of the new {@link Specification}.
     * @return The instance of the new {@link Builder}.
     */
    public static @NotNull Builder builder(@NotNull String id) {
        return new Builder(id);
    }

    /**
     * The {@link Builder} eases the creation of {@link Specification}s by providing a convenient interface.
     */
    public static class Builder {
        private final @NotNull Map<String, TypeSerializer<?>> rootTree = new HashMap<>();
        private final @NotNull Map<String, Specification> nestedTree = new HashMap<>();
        private final @NotNull String id;

        /**
         * Creates a new instance of a {@link Builder}.
         *
         * @param id The identifier of the new {@link Specification}.
         */
        public Builder(@NotNull String id) {
            Objects.requireNonNull(id);

            this.id = id;
        }

        /**
         * A builder variant of {@link Specification#add(String, TypeSerializer)}
         */
        public @NotNull Builder add(@NotNull String key, @NotNull TypeSerializer<?> serializer) {
            Objects.requireNonNull(key);
            Objects.requireNonNull(serializer);

            rootTree.putIfAbsent(key, serializer);

            return this;
        }

        /**
         * A builder variant of {@link Specification#add(String, Specification)}
         */
        public @NotNull Builder add(@NotNull String key, @NotNull Specification specification) {
            Objects.requireNonNull(key);
            Objects.requireNonNull(specification);

            nestedTree.putIfAbsent(key, specification);

            return this;
        }

        /**
         * Adds a new <code>byte</code> field.
         * @param key The name of that field.
         */
        public @NotNull Builder addByte(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.BYTE);
        }

        /**
         * Adds a new <code>short</code> field.
         * @param key The name of that field.
         */
        public @NotNull Builder addShort(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.SHORT);
        }

        /**
         * Adds a new <code>int</code> field.
         * @param key The name of that field.
         */
        public @NotNull Builder addInt(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.INT);
        }

        /**
         * Adds a new <code>long</code> field.
         * @param key The name of that field.
         */
        public @NotNull Builder addLong(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.LONG);
        }

        /**
         * Adds a new {@link UUID} field.
         * @param key The name of that field.
         */
        public @NotNull Builder addUUID(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.UUID);
        }

        /**
         * Adds a new <code>float</code> field.
         * @param key The name of that field.
         */
        public @NotNull Builder addFloat(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.FLOAT);
        }

        /**
         * Adds a new <code>double</code> field.
         * @param key The name of that field.
         */
        public @NotNull Builder addDouble(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.DOUBLE);
        }

        /**
         * Adds a new {@link String} field.
         * @param key The name of that field.
         */
        public @NotNull Builder addString(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.STRING);
        }

        /**
         * Adds a new byte-array field.
         * @param key The name of that field.
         */
        public @NotNull Builder addByteArray(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.BYTE_ARRAY);
        }

        /**
         * Adds a new int-array field.
         * @param key The name of that field.
         */
        public @NotNull Builder addIntArray(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.INT_ARRAY);
        }

        /**
         * Adds a new long-array field.
         * @param key The name of that field.
         */
        public @NotNull Builder addLongArray(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.LONG_ARRAY);
        }

        /**
         * Adds a new int-array field.
         * @param key The name of that field.
         */
        public @NotNull Builder addBool(@NotNull String key) {
            return add(key, BuiltinTypeSerializer.BOOL);
        }

        /**
         * Constructs the final {@link Specification} out of the collected data.
         *
         * @return The resulting {@link Specification} instance.
         */
        public @NotNull Specification build() {
            final Specification spec = new Specification(id);

            rootTree.forEach(spec::add);
            nestedTree.forEach(spec::add);

            return spec;
        }
    }
}
