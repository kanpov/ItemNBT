package com.redgrapefruit.itemnbt3.field.version;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SemanticFieldVersion implements FieldVersion {
    // the full literal of the version, e.g. "1.2.3"
    private final @NotNull String literal;

    // the semantic components
    private int major;
    private int minor;
    private int patch;

    private SemanticFieldVersion(@NotNull String literal) {
        if (setupSemanticVersion(literal)) {
            this.literal = literal;
        } else {
            throw new RuntimeException(literal + " is not a semantic version!");
        }
    }

    @Override
    public @NotNull String getLiteral() {
        return literal;
    }

    @Override
    public boolean equalsTo(@NotNull FieldVersion other) {
        if (other instanceof SemanticFieldVersion otherVersion) {
            return this.major == otherVersion.major && this.minor == otherVersion.minor && this.patch == otherVersion.patch;
        } else {
            return false;
        }
    }

    private boolean setupSemanticVersion(@NotNull String version) {
        if (!isSemantic(version)) return false;

        String[] pieces = version.split("\\.");

        major = Integer.parseInt(pieces[0]);
        minor = Integer.parseInt(pieces[1]);
        patch = Integer.parseInt(pieces[2]);

        return true;
    }

    @ApiStatus.Internal
    public static boolean isSemantic(@NotNull String version) {
        String[] pieces = version.split("\\.");

        if (pieces.length != 3) return false;

        for (String piece : pieces) {
            if (!isInteger(piece)) return false;
        }

        return true;
    }

    @ApiStatus.Internal
    public static boolean isInteger(@NotNull String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }

        return true;
    }

    public static @NotNull SemanticFieldVersion of(@NotNull String version) {
        Objects.requireNonNull(version, "Semantic version literal must not be null");

        return new SemanticFieldVersion(version);
    }

    public static @NotNull SemanticFieldVersion of(int major, int minor, int patch) {
        return new SemanticFieldVersion(major + "." + minor + "." + patch);
    }

    public static @NotNull SemanticFieldVersion of(int major, int minor) {
        return of(major, minor, 0);
    }

    public static @NotNull SemanticFieldVersion of(int major) {
        return of(major, 0, 0);
    }
}
