package com.redgrapefruit.itemnbt3.field.version;

import org.jetbrains.annotations.NotNull;

public class IntegerFieldVersion implements FieldVersion {
    private final int value;
    private final @NotNull String asLiteral;

    private IntegerFieldVersion(int value) {
        this.value = value;
        this.asLiteral = String.valueOf(value);
    }

    @Override
    public @NotNull String getLiteral() {
        return asLiteral;
    }

    @Override
    public boolean equalsTo(@NotNull FieldVersion other) {
        if (other instanceof IntegerFieldVersion otherInt) {
            return this.value == otherInt.value;
        } else {
            return false;
        }
    }

    public static @NotNull IntegerFieldVersion of(int version) {
        return new IntegerFieldVersion(version);
    }
}
