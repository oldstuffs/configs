package io.github.portlek.configs;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface Provided<T> {

    void set(@NotNull Object fieldValue, @NotNull Managed managed, @NotNull String path);

    @NotNull
    Optional<T> get(@NotNull Managed managed, @NotNull String path);

}
