package io.github.portlek.configs.util;

import io.github.portlek.configs.CfgSection;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface Provided<T> {

    void set(@NotNull T t, @NotNull CfgSection section, @NotNull String path);

    @NotNull
    default Optional<T> getWithField(@NotNull final T t, @NotNull final CfgSection section,
                                     @NotNull final String path) {
        return this.get(section, path);
    }

    @NotNull
    Optional<T> get(@NotNull CfgSection section, @NotNull String path);

}
