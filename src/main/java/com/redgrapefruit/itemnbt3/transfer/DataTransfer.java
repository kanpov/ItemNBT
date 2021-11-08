package com.redgrapefruit.itemnbt3.transfer;

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

public class DataTransfer {
    private final @NotNull Map<String, Pair<Method, Method>> rootTree = new HashMap<>();
    private final @NotNull Map<String, Pair<Method, DataTransfer>> nestedTree = new HashMap<>();
    private final @NotNull Supplier<Object> factory;

    private static final @NotNull Logger LOGGER = LogManager.getLogger();

    public DataTransfer(@NotNull Supplier<Object> factory) {
        this.factory = factory;
    }

    public void add(@NotNull String key, @NotNull Method getter, @NotNull Method setter, @NotNull Supplier<Object> factory) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(getter);
        Objects.requireNonNull(setter);
        Objects.requireNonNull(factory);

        rootTree.put(key, new Pair<>(getter, setter));
    }

    public void add(@NotNull String key, @NotNull Method getter, @NotNull DataTransfer model) {
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
            final DataTransfer model = pair.getRight();

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
}
