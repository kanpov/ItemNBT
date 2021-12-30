package com.redgrapefruit.itemnbt3.field;

import org.jetbrains.annotations.NotNull;

public interface FieldVersion {
    @NotNull String getLiteral();
    boolean equalsTo(@NotNull FieldVersion other);
}
