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

    public PathCalc(@NotNull String regex, @NotNull String separator, @NotNull String rawPath, @NotNull String parent,
        @NotNull String fallbackName) {
        this.regex = regex;
        this.separator = separator;
        this.rawPath = rawPath;
        this.parent = parent;
        this.fallbackName = fallbackName;
    }

    @NotNull
    public String value() {
        final String fieldPath;

        if (rawPath.isEmpty()) {
            if (!regex.isEmpty() && !separator.isEmpty()) {
                fieldPath = fallbackName.replace(regex, separator);
            } else {
                fieldPath = fallbackName;
            }
        } else {
            fieldPath = rawPath;
        }

        final String path;

        if (parent.isEmpty()) {
            path = fieldPath;
        } else if (parent.endsWith(".")) {
            path = parent + fieldPath;
        } else {
            path = parent + "." + fieldPath;
        }

        return path;
    }

}
