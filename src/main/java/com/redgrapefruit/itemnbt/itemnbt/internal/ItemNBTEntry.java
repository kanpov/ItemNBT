package com.redgrapefruit.itemnbt.itemnbt.internal;

import com.redgrapefruit.itemnbt.itemnbt.Deserializer;
import com.redgrapefruit.itemnbt.itemnbt.ItemType;
import com.redgrapefruit.itemnbt.itemnbt.Serializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class ItemNBTEntry {
    private final @NotNull ItemType type;
    private final @NotNull Serializer serializer;
    private final @NotNull Deserializer deserializer;

    public ItemNBTEntry(@NotNull ItemType type, @NotNull Serializer serializer, @NotNull Deserializer deserializer) {
        this.type = type;
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    public @NotNull ItemType getType() {
        return type;
    }

    public @NotNull Serializer getSerializer() {
        return serializer;
    }

    public @NotNull Deserializer getDeserializer() {
        return deserializer;
    }
}
