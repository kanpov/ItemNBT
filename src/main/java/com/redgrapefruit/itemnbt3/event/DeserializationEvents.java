package com.redgrapefruit.itemnbt3.event;

import com.redgrapefruit.itemnbt3.specification.DataCompound;
import com.redgrapefruit.itemnbt3.specification.Specification;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

/**
 * A set of built-in Fabric events for deserialization
 */
public class DeserializationEvents {
    // Called before deserializing custom data
    public static final Event<CustomEvent> CUSTOM_PRE_DESERIALIZE = EventFactory.createArrayBacked(CustomEvent.class,
            (listeners) -> (stack, nbt) -> {
                for (CustomEvent listener : listeners) {
                    listener.event(stack, nbt);
                }
            }
    );

    // Called after deserializing custom data
    public static final Event<CustomEvent> CUSTOM_POST_DESERIALIZE = EventFactory.createArrayBacked(CustomEvent.class,
            (listeners) -> (stack, nbt) -> {
                for (CustomEvent listener : listeners) {
                    listener.event(stack, nbt);
                }
            }
    );

    // Called before deserializing DataCompounds
    public static final Event<DefaultEvent> DEFAULT_PRE_DESERIALIZE = EventFactory.createArrayBacked(DefaultEvent.class,
            (listeners) -> (stack, spec, nbt, data) -> {
                for (DefaultEvent listener : listeners) {
                    listener.event(stack, spec, nbt, data);
                }
            }
    );

    // Called after deserializing DataCompounds
    public static final Event<DefaultEvent> DEFAULT_POST_DESERIALIZE = EventFactory.createArrayBacked(DefaultEvent.class,
            (listeners) -> (stack, spec, nbt, data) -> {
                for (DefaultEvent listener : listeners) {
                    listener.event(stack, spec, nbt, data);
                }
            }
    );

    // Called before deserializing linked DataCompounds
    public static final Event<LinkedEvent> LINKED_PRE_DESERIALIZE = EventFactory.createArrayBacked(LinkedEvent.class,
            (listeners) -> (stack, spec, nbt, data, instance) -> {
                for (LinkedEvent listener : listeners) {
                    listener.event(stack, spec, nbt, data, instance);
                }
            }
    );

    // Called after deserializing linked DataCompounds
    public static final Event<LinkedEvent> LINKED_POST_DESERIALIZE = EventFactory.createArrayBacked(LinkedEvent.class,
            (listeners) -> (stack, spec, nbt, data, instance) -> {
                for (LinkedEvent listener : listeners) {
                    listener.event(stack, spec, nbt, data, instance);
                }
            }
    );

    public interface CustomEvent {
        void event(@NotNull ItemStack stack, @NotNull NbtCompound nbt);
    }

    public interface DefaultEvent {
        void event(@NotNull ItemStack stack, @NotNull Specification spec, @NotNull NbtCompound nbt, @NotNull DataCompound data);
    }

    public interface LinkedEvent  {
        void event(@NotNull ItemStack stack, @NotNull Specification spec, @NotNull NbtCompound nbt, @NotNull DataCompound data, @NotNull Object instance);
    }
}
