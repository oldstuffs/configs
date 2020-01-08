package io.github.portlek.configs;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public abstract class Linked {

    @NotNull
    private Locale locale;

    public Linked(@NotNull Locale locale) {
        this.locale = locale;
    }

    @NotNull
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(@NotNull Locale locale) {
        this.locale = locale;
    }

}
