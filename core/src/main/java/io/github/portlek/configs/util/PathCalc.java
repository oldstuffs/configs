package io.github.portlek.configs.util;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class PathCalc {

    @NotNull
    private final String regex;

    @NotNull
    private final String separator;

    @NotNull
    private final String rawpath;

    @NotNull
    private final String fallback;

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
