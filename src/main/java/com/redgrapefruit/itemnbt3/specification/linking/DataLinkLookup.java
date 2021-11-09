package com.redgrapefruit.itemnbt3.specification.linking;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ApiStatus.Internal
public class DataLinkLookup {
    private static final @NotNull Map<Class<?>, DataLink> registry = new HashMap<>();

    private DataLinkLookup() {
        throw new RuntimeException("DataLinkRegistry is not meant to be instantiated");
    }

    public static void register(@NotNull Class<?> clazz, @NotNull DataLink link) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(link);

        registry.put(clazz, link);
    }

    public static boolean lacks(@NotNull Class<?> clazz) {
        Objects.requireNonNull(clazz);

        return !registry.containsKey(clazz);
    }

    public static @NotNull DataLink get(@NotNull Class<?> clazz) {
        Objects.requireNonNull(clazz);

        return registry.get(clazz);
    }
}
