package io.github.portlek.configs;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public class NukkitLinkedManaged extends NukkitManaged implements LnkdFlManaged {

    @SafeVarargs
    public NukkitLinkedManaged(@NotNull final Supplier<String> chosen,
                               @NotNull final Map.Entry<String, Object>... objects) {
        super(new LinkedFileManaged(chosen), objects);
    }

    @NotNull
    @Override
    public final LnkdFlManaged getBase() {
        return (LnkdFlManaged) super.getBase();
    }

    @NotNull
    @Override
    public final <T> T match(@NotNull final Function<String, Optional<T>> function) {
        return this.getBase().match(function);
    }

    @NotNull
    @Override
    public final Supplier<String> getChosen() {
        return this.getBase().getChosen();
    }

}
