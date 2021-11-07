package com.redgrapefruit.itemnbt3.specification.generation;

import com.redgrapefruit.itemnbt3.specification.Specification;
import com.redgrapefruit.itemnbt3.specification.TypeSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * An experimental feature that generates a {@link Specification} for a class automatically with the use
 * of Java's reflection features.
 */
@ApiStatus.Experimental
public class SpecificationGenerator {
    private SpecificationGenerator() {
        throw new RuntimeException("SpecificationGenerator is not meant to be instantiated.");
    }

    public static @NotNull Specification generate(@NotNull Class<?> clazz) {
        Objects.requireNonNull(clazz);

        final Specification spec = new Specification(clazz.getSimpleName());

        for (Field field : clazz.getDeclaredFields()) {
            if (SerializerRegistry.contains(field.getType())) {
                final TypeSerializer<?> serializer = SerializerRegistry.get(field.getType());
                spec.add(field.getName(), serializer);
            } else {
                spec.add(field.getName(), generate(field.getType()));
            }
        }

        return spec;
    }
}
