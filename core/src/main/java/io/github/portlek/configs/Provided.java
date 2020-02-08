package io.github.portlek.configs;

import org.jetbrains.annotations.NotNull;

public interface Provided<T> {

    void set(@NotNull Object fieldValue, @NotNull Managed managed, @NotNull String path);

    @NotNull
    T get(@NotNull Managed managed, @NotNull String path);

}
