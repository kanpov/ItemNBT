package com.redgrapefruit.itemnbt3.field;

import com.redgrapefruit.itemnbt3.field.version.FieldVersion;
import com.redgrapefruit.itemnbt3.field.version.SemanticFieldVersion;
import com.redgrapefruit.itemnbt3.util.NoRemoval;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public interface FieldSerializer<T> {
    // Mandatory to implement

    @NotNull T readDataFromNbt(
            @NotNull NbtCompound dedicatedNbt,
            @NotNull @NoRemoval NbtCompound metadataNbt,
            @NotNull @NoRemoval NbtCompound rootNbt);

    void writeDataToNbt(
            @NotNull NbtCompound dedicatedNbt,
            @NotNull @NoRemoval NbtCompound metadataNbt,
            @NotNull @NoRemoval NbtCompound rootNbt,
            @NotNull T value);

    void writeDefaultValue(
            @NotNull NbtCompound dedicatedNbt,
            @NotNull @NoRemoval NbtCompound metadataNbt,
            @NotNull @NoRemoval NbtCompound rootNbt);

    @NotNull Identifier getId();

    // Optional to implement

    default @NotNull FieldVersion getExpectedDataVersion() {
        return SemanticFieldVersion.of(1, 0, 0); // the default version is 1.0.0
    }

    default boolean shouldWriteDefaultValue(
            @NotNull @NoRemoval NbtCompound dedicatedNbt,
            @NotNull @NoRemoval NbtCompound metadataNbt,
            @NotNull @NoRemoval NbtCompound rootNbt) {

        return dedicatedNbt.isEmpty();
    }

    default void setupDefaultMetadata(
            @NotNull @NoRemoval NbtCompound dedicatedNbt,
            @NotNull NbtCompound metadataNbt,
            @NotNull @NoRemoval NbtCompound rootNbt
    ) {
        // Serialized version
        metadataNbt.putString("Version", getExpectedDataVersion().getLiteral());
        // Serializer ID
        metadataNbt.putString("Serializer", getId().toString());
    }

    default boolean tryPortData(
            @NotNull NbtCompound dedicatedNbt,
            @NotNull NbtCompound metadataNbt,
            @NotNull NbtCompound rootNbt,
            @NotNull FieldVersion sourceVersion
    ) {
        return false; // by default, no porting is implemented
    }

    default boolean tryPortFromV1Format(
            @NotNull NbtCompound rootNbt,
            @NotNull NbtElement legacyData,
            @NotNull String key
    ) {
        // Remove the old key, the legacyData parameter, holding the old data, still persists
        rootNbt.remove(key);

        // Create the metadata NBT inside the root NBT
        NbtCompound metadataNbt = new NbtCompound();
        rootNbt.put(key, metadataNbt);

        // Create the dedicated value NBT inside the metadata NBT
        NbtCompound dedicatedNbt = new NbtCompound();
        metadataNbt.put("Content", dedicatedNbt);
        dedicatedNbt.put("Value", legacyData);

        return true;
    }
}
