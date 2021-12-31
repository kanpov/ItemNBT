package com.redgrapefruit.itemnbt3.field.version;

import org.jetbrains.annotations.NotNull;

/**
 * A {@link FieldVersion} determines the serialization versioning of a field.
 */
public interface FieldVersion {
    /**
     * @return The string value of this version, which will put into NBT.
     */
    @NotNull String getLiteral();

    /**
     * Checks if two {@link FieldVersion}s are equal.<br>
     * <b>Note:</b> this doesn't differentiate between whether one version is bigger or smaller, meaning
     * it might be used both for backwards and forwards compatibility between NBT data.
     *
     * @param other The other {@link FieldVersion} to compare against.
     * @return Whether this {@link FieldVersion} is equal to the other {@link FieldVersion}
     */
    boolean equalsTo(@NotNull FieldVersion other);

    static @NotNull FieldVersion parseVersion(@NotNull String literal) {
        if (SemanticFieldVersion.isInteger(literal)) {
            return IntegerFieldVersion.of(Integer.parseInt(literal));
        }

        if (SemanticFieldVersion.isSemantic(literal)) {
            return SemanticFieldVersion.of(literal);
        }

        return LiteralFieldVersion.of(literal);
    }
}
