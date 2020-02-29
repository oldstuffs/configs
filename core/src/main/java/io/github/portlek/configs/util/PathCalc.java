package io.github.portlek.configs.util;

import org.jetbrains.annotations.NotNull;

public final class PathCalc {

    @NotNull
    private final String regex;

    @NotNull
    private final String separator;

    @NotNull
    private final String rawPath;

    @NotNull
    private final String parent;

    @NotNull
    private final String fallbackName;

    public PathCalc(@NotNull final String regex, @NotNull final String separator, @NotNull final String rawPath, @NotNull final String parent,
                    @NotNull final String fallbackName) {
        this.regex = regex;
        this.separator = separator;
        this.rawPath = rawPath;
        this.parent = parent;
        this.fallbackName = fallbackName;
    }

    @NotNull
    public String value() {
        final String fieldPath;

        if (this.rawPath.isEmpty()) {
            if (!this.regex.isEmpty() && !this.separator.isEmpty()) {
                fieldPath = this.fallbackName.replace(this.regex, this.separator);
            } else {
                fieldPath = this.fallbackName;
            }
        } else {
            fieldPath = this.rawPath;
        }

        final String path;

        if (this.parent.isEmpty()) {
            path = fieldPath;
        } else if (this.parent.endsWith(".")) {
            path = this.parent + fieldPath;
        } else {
            path = this.parent + "." + fieldPath;
        }

        return path;
    }

}
