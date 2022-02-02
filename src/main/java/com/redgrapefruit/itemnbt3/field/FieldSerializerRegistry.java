package com.redgrapefruit.itemnbt3.field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class FieldSerializerRegistry {
    private static final @NotNull Map<Class<?>, FieldSerializer<?>> registry = new HashMap<>();

    private static final @NotNull Logger LOGGER = LogManager.getLogger();

    private FieldSerializerRegistry() {
        throw new RuntimeException("FieldSerializerRegistry is not meant to be instantiated.");
    }
    
    static {
        registry.put(byte.class, BuiltinFieldSerializer.BYTE);
        registry.put(short.class, BuiltinFieldSerializer.SHORT);
        registry.put(int.class, BuiltinFieldSerializer.INT);
        registry.put(long.class, BuiltinFieldSerializer.LONG);
        registry.put(UUID.class, BuiltinFieldSerializer.UUID);
        registry.put(float.class, BuiltinFieldSerializer.FLOAT);
        registry.put(double.class, BuiltinFieldSerializer.DOUBLE);
        registry.put(String.class, BuiltinFieldSerializer.STRING);
        registry.put(byte[].class, BuiltinFieldSerializer.BYTE_ARRAY);
        registry.put(int[].class, BuiltinFieldSerializer.INT_ARRAY);
        registry.put(long[].class, BuiltinFieldSerializer.LONG_ARRAY);
        registry.put(boolean.class, BuiltinFieldSerializer.BOOL);
    }

    /**
     * Registers a custom serializer for a non-composite type.
     *
     * @param clazz The class of the non-composite type.
     * @param serializer The serializer for that type.
     * @param <T> That type in generics.
     */
    public static <T> void register(@NotNull Class<T> clazz, @NotNull FieldSerializer<T> serializer) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(serializer);

        if (registry.containsKey(clazz)) {
            LOGGER.warn("Tried to register duplicate serializer");
            return;
        }

        registry.put(clazz, serializer);
    }

    @ApiStatus.Internal
    public static boolean contains(@NotNull Class<?> clazz) {
        return registry.containsKey(clazz);
    }

    @ApiStatus.Internal
    public static FieldSerializer<?> get(@NotNull Class<?> clazz) {
        return registry.get(clazz);
    }
}
