package io.github.portlek.configs.util;

import org.jetbrains.annotations.NotNull;

public final class PathCalc {

    @NotNull
    private final String regex;

    @NotNull
    private final String separator;

    @NotNull
    private final String rawpath;

    @NotNull
    private final String fallback;

    public PathCalc(@NotNull final String rgx, @NotNull final String sprtr, @NotNull final String rwpth,
                    @NotNull final String fllbcknm) {
        this.regex = rgx;
        this.separator = sprtr;
        this.rawpath = rwpth;
        this.fallback = fllbcknm;
    }

    @NotNull
    public String value() {
        final String fieldpath;
        if (this.rawpath.isEmpty()) {
            if (!this.regex.isEmpty() && !this.separator.isEmpty()) {
                fieldpath = this.fallback.replace(this.regex, this.separator);
            } else {
                fieldpath = this.fallback;
            }
        } else {
            fieldpath = this.rawpath;
        }
        return fieldpath;
    }

}
