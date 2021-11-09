package com.redgrapefruit.itemnbt3.event;

import com.redgrapefruit.itemnbt3.specification.DataCompound;
import com.redgrapefruit.itemnbt3.specification.Specification;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

/**
 * A set of Fabric events related to forward and backward linking of serialized data.
 */
public class LinkingEvents {
    // Called before forward-linking
    public static final Event<LinkingEvent> PRE_FORWARD_LINK = EventFactory.createArrayBacked(LinkingEvent.class,
            (listeners) -> (stack, spec, nbt, data, instance) -> {
                for (LinkingEvent listener : listeners) {
                    listener.event(stack, spec, nbt, data, instance);
                }
            }
    );

    // Called after forward-linking
    public static final Event<LinkingEvent> POST_FORWARD_LINK = EventFactory.createArrayBacked(LinkingEvent.class,
            (listeners) -> (stack, spec, nbt, data, instance) -> {
                for (LinkingEvent listener : listeners) {
                    listener.event(stack, spec, nbt, data, instance);
                }
            }
    );

    // Called before backward-linking
    public static final Event<LinkingEvent> PRE_BACKWARD_LINK = EventFactory.createArrayBacked(LinkingEvent.class,
            (listeners) -> (stack, spec, nbt, data, instance) -> {
                for (LinkingEvent listener : listeners) {
                    listener.event(stack, spec, nbt, data, instance);
                }
            }
    );

    // Called after backward-linking
    public static final Event<LinkingEvent> POST_BACKWARD_LINK = EventFactory.createArrayBacked(LinkingEvent.class,
            (listeners) -> (stack, spec, nbt, data, instance) -> {
                for (LinkingEvent listener : listeners) {
                    listener.event(stack, spec, nbt, data, instance);
                }
            }
    );

    public interface LinkingEvent {
        void event(@NotNull ItemStack stack, @NotNull Specification spec, @NotNull NbtCompound nbt, @NotNull DataCompound data, @NotNull Object instance);
    }
}
