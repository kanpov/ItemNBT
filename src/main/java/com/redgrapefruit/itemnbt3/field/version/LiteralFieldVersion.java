package com.redgrapefruit.itemnbt3.field.version;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LiteralFieldVersion implements FieldVersion {
    private final @NotNull String value;

    private LiteralFieldVersion(@NotNull String value) {
        this.value = value;
    }

    @Override
    public @NotNull String getLiteral() {
        return value;
    }

    @Override
    public boolean equalsTo(@NotNull FieldVersion other) {
        return value.compareTo(other.getLiteral()) == 0;
    }

    public static @NotNull LiteralFieldVersion of(@NotNull String literal) {
        Objects.requireNonNull(literal, "Version literal must not be null");

        return new LiteralFieldVersion(literal);
    }
}
