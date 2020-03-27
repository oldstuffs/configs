package io.github.portlek.configs;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface Provided<T> {

    void set(@NotNull T fieldvalue, @NotNull ConfigSection section, @NotNull String path);

    @NotNull
    default Optional<T> getWithField(@NotNull final T fieldvalue, @NotNull final ConfigSection section,
                                     @NotNull final String path) {
        return this.get(section, path);
    }

    @NotNull
    Optional<T> get(@NotNull ConfigSection section, @NotNull String path);

}
