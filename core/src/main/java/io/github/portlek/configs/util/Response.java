package io.github.portlek.configs.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class Response<T> {

    @Nullable
    private final T object;

    public Response(@Nullable T object) {
        this.object = object;
    }

    @NotNull
    public static <T> Response<T> of(@NotNull T t) {
        return new Response<>(t);
    }

    public static <T> Response<T> empty() {
        return new Response<>(null);
    }

    @NotNull
    public Optional<T> getResponse() {
        return Optional.ofNullable(object);
    }

}