package com.redgrapefruit.itemnbt3.specification.generation;

import com.redgrapefruit.itemnbt3.specification.BuiltinTypeSerializer;
import com.redgrapefruit.itemnbt3.specification.TypeSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SerializerRegistry {
    private static final @NotNull Map<Class<?>, TypeSerializer<?>> registry = new HashMap<>();

    static {
        registry.put(byte.class, BuiltinTypeSerializer.BYTE);
        registry.put(short.class, BuiltinTypeSerializer.SHORT);
        registry.put(int.class, BuiltinTypeSerializer.INT);
        registry.put(long.class, BuiltinTypeSerializer.LONG);
        registry.put(UUID.class, BuiltinTypeSerializer.UUID);
        registry.put(float.class, BuiltinTypeSerializer.FLOAT);
        registry.put(double.class, BuiltinTypeSerializer.DOUBLE);
        registry.put(String.class, BuiltinTypeSerializer.STRING);
        registry.put(byte[].class, BuiltinTypeSerializer.BYTE_ARRAY);
        registry.put(int[].class, BuiltinTypeSerializer.INT_ARRAY);
        registry.put(long[].class, BuiltinTypeSerializer.LONG_ARRAY);
        registry.put(boolean.class, BuiltinTypeSerializer.BOOL);
    }

    private static final @NotNull Logger LOGGER = LogManager.getLogger();

    private SerializerRegistry() {
        throw new RuntimeException("SerializerRegistry is not meant to be instantiated.");
    }

    public static <T> void register(@NotNull Class<T> clazz, @NotNull TypeSerializer<T> serializer) {
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
    public static TypeSerializer<?> get(@NotNull Class<?> clazz) {
        return registry.get(clazz);
    }
}
