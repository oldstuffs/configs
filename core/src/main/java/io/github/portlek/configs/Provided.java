package io.github.portlek.configs;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface Provided<T> {

    default void set(@NotNull final T t, @NotNull final ConfigSection section) {
        this.set(t, section, "");
    }

    void set(@NotNull T t, @NotNull ConfigSection section, @NotNull String path);

    @NotNull
    default Optional<T> getWithField(@NotNull final T t, @NotNull final ConfigSection section) {
        return this.getWithField(t, section, "");
    }

    @NotNull
    default Optional<T> getWithField(@NotNull final T t, @NotNull final ConfigSection section,
                                     @NotNull final String path) {
        return this.get(section, path);
    }

    @NotNull
    Optional<T> get(@NotNull ConfigSection section, @NotNull String path);

    @NotNull
    default Optional<T> get(@NotNull final ConfigSection section) {
        return this.get(section, "");
    }

}
