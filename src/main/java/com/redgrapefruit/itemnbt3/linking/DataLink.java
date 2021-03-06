package com.redgrapefruit.itemnbt3.linking;

import com.redgrapefruit.itemnbt3.serializer.SerializerRegistry;
import com.redgrapefruit.itemnbt3.specification.DataCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A {@link DataLink} ports your serialized data from a {@link DataCompound} to a Java POJO.
 */
public final class DataLink {
    private final @NotNull Map<String, Field> fields = new HashMap<>();
    private final @NotNull Map<String, Field> composites = new HashMap<>();

    private static final @NotNull Logger LOGGER = LogManager.getLogger();
    /**
     * Adds a regular field to the link.
     *
     * @param address The NBT address of the field.
     * @param field The reflect-field.
     */
    public void addField(@NotNull String address, @NotNull Field field) {
        Objects.requireNonNull(address);
        Objects.requireNonNull(field);

        fields.put(address, field);
    }

    /**
     * Adds a composite field to the link.
     *
     * @param address The NBT address of the field.
     * @param field The reflect-field.
     */
    public void addComposite(@NotNull String address, @NotNull Field field) {
        Objects.requireNonNull(address);
        Objects.requireNonNull(field);

        composites.put(address, field);
    }

    /**
     * Ports the data from a {@link DataCompound} to a POJO.
     *
     * @param data The {@link DataCompound} with the original data.
     * @param instance The POJO instance.
     */
    public void forwardLink(@NotNull DataCompound data, @NotNull Object instance) {
        fields.forEach((key, field) -> {
            try {
                field.set(instance, data.get(key));
            } catch (IllegalAccessException e) {
                LOGGER.error("Could not forward-link field " + field.getName() + ". Illegal access, make it public");
            }
        });

        composites.forEach((key, field) -> {
            final Class<?> otherClazz = field.getType();

            if (DataLinkLookup.lacks(otherClazz)) {
                LOGGER.error("Trying to lookup non-existing DataLink for field " + field.getName() + " of type " + otherClazz.getSimpleName());
            }

            final DataLink otherLink = DataLinkLookup.get(otherClazz);
            final DataCompound otherCompound = data.getOrCreateCompound(key);
            Object otherInstance = null;

            try {
                otherInstance = field.get(instance);
            } catch (IllegalAccessException e) {
                LOGGER.error("Could not forward-link composite field " + field.getName() + ". Illegal access, make it public!");
            }

            otherInstance = fallbackField(otherInstance, otherClazz);
            Objects.requireNonNull(otherInstance);

            otherLink.forwardLink(otherCompound, otherInstance);

            try {
                field.set(instance, otherInstance);
            } catch (IllegalAccessException e) {
                LOGGER.error("Could not forward-link composite field " + field.getName() + ". Illegal access, make it public!");
            }
        });
    }

    /**
     * Ports the data from a POJO back into the {@link DataCompound}.
     *
     * @param data The target {@link DataCompound}.
     * @param instance The POJO instance.
     */
    public void backwardLink(@NotNull DataCompound data, @NotNull Object instance) {
        fields.forEach((key, field) -> {
            Object value = null;

            try {
                value = field.get(instance);
            } catch (IllegalAccessException e) {
                LOGGER.error("Could not backward-link field " + field.getName() + ". Illegal access, make it public");
            }

            Objects.requireNonNull(value);

            data.put(key, value);
        });

        composites.forEach((key, field) -> {
            final Class<?> otherClazz = field.getType();

            if (DataLinkLookup.lacks(otherClazz)) {
                LOGGER.error("Trying to lookup non-existing DataLink for field " + field.getName() + " of type " + otherClazz.getSimpleName());
            }

            final DataLink otherLink = DataLinkLookup.get(otherClazz);
            final DataCompound otherCompound = data.getOrCreateCompound(key);
            Object otherInstance = null;

            try {
                otherInstance = field.get(instance);
            } catch (IllegalAccessException e) {
                LOGGER.error("Could not forward-link composite field " + field.getName() + ". Illegal access, make it public!");
            }

            otherInstance = fallbackField(otherInstance, otherClazz);
            Objects.requireNonNull(otherInstance);

            otherLink.backwardLink(otherCompound, otherInstance);

            try {
                field.set(instance, otherInstance);
            } catch (IllegalAccessException e) {
                LOGGER.error("Could not forward-link composite field " + field.getName() + ". Illegal access, make it public!");
            }
        });
    }

    /**
     * Automatically generates a {@link DataLink} using Java Reflection and registers it in {@link DataLinkLookup}.
     *
     * @param clazz The target POJO's class.
     * @return The generated {@link DataLink}.
     */
    public static @NotNull DataLink create(@NotNull Class<?> clazz) {
        Objects.requireNonNull(clazz);

        if (clazz.isAnnotationPresent(Auto.class)) {
            return createAutomatic(clazz);
        } else {
            return createManual(clazz);
        }
    }

    private static @NotNull DataLink createAutomatic(@NotNull Class<?> clazz) {
        final DataLink link = new DataLink();

        final Field[] fields = clazz.isAnnotationPresent(AllowInheritance.class) ? clazz.getFields() : clazz.getDeclaredFields();

        for (Field field : fields) {
            if (!Modifier.isPublic(field.getModifiers())) continue;

            if (SerializerRegistry.contains(field.getType())) {
                link.addField(field.getName(), field);
            } else {
                link.addComposite(field.getName(), field);
            }
        }

        DataLinkLookup.register(clazz, link);
        return link;
    }

    private static @NotNull DataLink createManual(@NotNull Class<?> clazz) {
        final DataLink link = new DataLink();

        final Field[] fields = clazz.isAnnotationPresent(AllowInheritance.class) ? clazz.getFields() : clazz.getDeclaredFields();

        for (Field field : fields) {
            if (!Modifier.isPublic(field.getModifiers())) continue;

            // Um yeah, name conflicts are bad
            if (field.isAnnotationPresent(com.redgrapefruit.itemnbt3.linking.Field.class)) {
                final com.redgrapefruit.itemnbt3.linking.Field annotation = field.getAnnotation(com.redgrapefruit.itemnbt3.linking.Field.class);

                String name = annotation.name();
                if (annotation.name().equals("^NULL")) {
                    name = field.getName();
                }

                link.addField(name, field);
            }

            if (field.isAnnotationPresent(Composite.class)) {
                final Composite annotation = field.getAnnotation(Composite.class);

                String name = annotation.name();
                if (annotation.name().equals("^NULL")) {
                    name = field.getName();
                }

                link.addComposite(name, field);
            }
        }

        DataLinkLookup.register(clazz, link);
        return link;
    }

    private static @NotNull Supplier<Object> createFactoryFromFirstConstructor(@NotNull Class<?> clazz) {
        return () -> {
            try {
                return clazz.getDeclaredConstructors()[0].newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Reflective generation of a data link failed while creating factory from constructor");
            }
        };
    }

    private static Object fallbackField(@Nullable Object value, @NotNull Class<?> clazz) {
        final Supplier<Object> fallback = createFactoryFromFirstConstructor(clazz);

        if (value == null) {
            value = fallback.get();
        }

        return value;
    }
}
