package com.redgrapefruit.itemnbt3.specification.linking;

import com.redgrapefruit.itemnbt3.specification.DataCompound;
import net.minecraft.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class DataLinker {
    private final @NotNull Map<String, Pair<Method, Method>> rootTree = new HashMap<>();
    private final @NotNull Map<String, Pair<Method, DataLinker>> nestedTree = new HashMap<>();
    private final @NotNull Supplier<Object> factory;

    private static final @NotNull Logger LOGGER = LogManager.getLogger();

    public DataLinker(@NotNull Supplier<Object> factory) {
        this.factory = factory;
    }

    public void add(@NotNull String key, @NotNull Method getter, @NotNull Method setter) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(getter);
        Objects.requireNonNull(setter);

        rootTree.put(key, new Pair<>(getter, setter));
    }

    public void add(@NotNull String key, @NotNull Method getter, @NotNull DataLinker model) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(model);

        nestedTree.put(key, new Pair<>(getter, model));
    }

    public void fromCompound(@NotNull DataCompound compound, @NotNull Object instance) {
        rootTree.forEach((key, pair) -> {
            final Method setter = pair.getRight();
            final Object value = compound.get(key);

            try {
                setter.invoke(instance, value);
            } catch (InvocationTargetException e) {
                LOGGER.error("Received a chained exception while invoking setter method");
            } catch (IllegalAccessException e) {
                LOGGER.error("Illegal access to setter method: please, make it public");
            }
        });

        nestedTree.forEach((key, pair) -> {
            final DataLinker model = pair.getRight();

            final DataCompound subCompound = compound.getOrCreateCompound(key);
            model.fromCompound(subCompound, model.getFactory().get());
        });
    }

    public void toCompound(@NotNull DataCompound compound, @NotNull Object instance) {
        rootTree.forEach((key, pair) -> {
            final Method getter = pair.getLeft();

            Object value = null;

            try {
                value = getter.invoke(instance);
            } catch (InvocationTargetException e) {
                LOGGER.error("Received a chained exception while invoking getter method");
            } catch (IllegalAccessException e) {
                LOGGER.error("Illegal access to getter method: please, make it public");
            }

            Objects.requireNonNull(value, "Getter failed");

            compound.put(key, value);
        });

        nestedTree.forEach((key, pair) -> {
            final Method getter = pair.getLeft();

            Object value = null;

            try {
                value = getter.invoke(instance);
            } catch (InvocationTargetException e) {
                LOGGER.error("Received a chained exception while invoking getter method");
            } catch (IllegalAccessException e) {
                LOGGER.error("Illegal access to getter method: please, make it public");
            }

            Objects.requireNonNull(value, "Getter failed");

            final DataCompound subCompound = compound.getOrCreateCompound(key);
            toCompound(subCompound, value);
        });
    }

    public @NotNull Supplier<Object> getFactory() {
        return factory;
    }

    public static @NotNull DataLinker create(@NotNull Class<?> clazz) {
        Objects.requireNonNull(clazz);

        // Use first declared constructor for factory supplier
        final DataLinker linker = new DataLinker(() -> {
            try {
                return clazz.getDeclaredConstructors()[0].newInstance();
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });

        final Map<String, Pair<Method, Method>> finds = new HashMap<>();

        // Scan methods
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Getter.class)) {
                final Getter annotation = method.getAnnotation(Getter.class);

                Pair<Method, Method> pair = finds.get(annotation.to());

                if (pair == null) {
                    pair = new Pair<>(method, null);
                } else {
                    pair.setLeft(method);
                }

                finds.put(annotation.to(), pair);
            }

            if (method.isAnnotationPresent(Setter.class)) {
                final Setter annotation = method.getAnnotation(Setter.class);

                Pair<Method, Method> pair = finds.get(annotation.to());

                if (pair == null) {
                    pair = new Pair<>(null, method);
                } else {
                    pair.setRight(method);
                }

                finds.put(annotation.to(), pair);
            }
        }

        finds.forEach((key, pair) -> {
            if (pair.getLeft() == null || pair.getRight() == null) {
                throw new RuntimeException("Failed to generate linker with the means of reflection");
            }

            linker.add(key, pair.getLeft(), pair.getRight());
        });

        return linker;
    }
}
