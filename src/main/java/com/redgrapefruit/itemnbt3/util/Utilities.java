package com.redgrapefruit.itemnbt3.util;

import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Utilities {
    public static NbtCompound getOrCreateSubNbt(@NotNull NbtCompound nbt, @NotNull String name) {
        Objects.requireNonNull(nbt);

        NbtCompound output;
        if (!nbt.contains(name)) {
            output = new NbtCompound();
            nbt.put(name, output);
        } else {
            output = nbt.getCompound(name);
        }

        return output;
    }
}
