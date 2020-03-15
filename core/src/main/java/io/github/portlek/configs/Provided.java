package io.github.portlek.configs;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface Provided<T> {

    void set(@NotNull T fieldvalue, @NotNull Managed managed, @NotNull String path);

    @NotNull
    default Optional<T> getWithField(@NotNull final T fieldvalue, @NotNull final Managed managed,
                                     @NotNull final String path) {
        return this.get(managed, path);
    }

    @NotNull
    Optional<T> get(@NotNull Managed managed, @NotNull String path);

}
