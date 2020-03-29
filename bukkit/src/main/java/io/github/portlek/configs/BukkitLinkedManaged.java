package io.github.portlek.configs;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;

public class BukkitLinkedManaged extends BukkitManaged implements LnkdFlManaged {

    @SafeVarargs
    public BukkitLinkedManaged(@NotNull final String chosen,
                               @NotNull final Map.Entry<String, Object>... objects) {
        super(new LinkedFileManaged(chosen), objects);
    }

    @NotNull
    @Override
    public final <T> T match(@NotNull final Function<String, Optional<T>> function) {
        return this.getBase().match(function);
    }

    @NotNull
    @Override
    public final LnkdFlManaged getBase() {
        return (LnkdFlManaged) super.getBase();
    }

    @Override
    public final void setup(final @NotNull File file, final @NotNull FileConfiguration fileConfiguration) {
        this.getBase().setup(file, fileConfiguration);
    }

    @NotNull
    @Override
    public final String getChosen() {
        return this.getBase().getChosen();
    }


}
