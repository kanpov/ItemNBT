package com.redgrapefruit.itemnbt3.event;

import com.redgrapefruit.itemnbt3.specification.DataCompound;
import com.redgrapefruit.itemnbt3.specification.Specification;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

/**
 * A set of built-in Fabric events for serialization
 */
public class SerializationEvents {
    // Called before serializing custom data
    public static final Event<CustomEvent> CUSTOM_PRE_SERIALIZE = EventFactory.createArrayBacked(CustomEvent.class,
        (listeners) -> (stack, nbt) -> {
            for (CustomEvent listener : listeners) {
                listener.event(stack, nbt);
            }
        }
    );

    // Called after serializing custom data
    public static final Event<CustomEvent> CUSTOM_POST_SERIALIZE = EventFactory.createArrayBacked(CustomEvent.class,
            (listeners) -> (stack, nbt) -> {
                for (CustomEvent listener : listeners) {
                    listener.event(stack, nbt);
                }
            }
    );

    // Called before serializing DataCompounds
    public static final Event<DefaultEvent> DEFAULT_PRE_SERIALIZE = EventFactory.createArrayBacked(DefaultEvent.class,
            (listeners) -> (stack, spec, nbt, data) -> {
                for (DefaultEvent listener : listeners) {
                    listener.event(stack, spec, nbt, data);
                }
            }
    );

    // Called after serializing DataCompounds
    public static final Event<DefaultEvent> DEFAULT_POST_SERIALIZE = EventFactory.createArrayBacked(DefaultEvent.class,
            (listeners) -> (stack, spec, nbt, data) -> {
                for (DefaultEvent listener : listeners) {
                    listener.event(stack, spec, nbt, data);
                }
            }
    );

    // Called before serializing linked DataCompounds
    public static final Event<LinkedEvent> LINKED_PRE_SERIALIZE = EventFactory.createArrayBacked(LinkedEvent.class,
            (listeners) -> (stack, spec, nbt, data, instance) -> {
                for (LinkedEvent listener : listeners) {
                    listener.event(stack, spec, nbt, data, instance);
                }
            }
    );

    // Called after serializing linked DataCompounds
    public static final Event<LinkedEvent> LINKED_POST_SERIALIZE = EventFactory.createArrayBacked(LinkedEvent.class,
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
