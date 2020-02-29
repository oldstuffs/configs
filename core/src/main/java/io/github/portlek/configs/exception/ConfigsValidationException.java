package io.github.portlek.configs.exception;

import org.jetbrains.annotations.NotNull;

public class ConfigsValidationException extends RuntimeException {

    private final long serialVersionUID = -7961367314553460325L;

    public ConfigsValidationException(@NotNull final Throwable throwable, @NotNull final String... messages) {
        super(throwable);
        for (final String part : messages) {
            System.err.println(part);
        }
    }

    public ConfigsValidationException(@NotNull final String... messages) {
        for (final String part : messages) {
            System.err.println(part);
        }
    }

}