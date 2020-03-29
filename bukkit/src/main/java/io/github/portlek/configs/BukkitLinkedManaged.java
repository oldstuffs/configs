package io.github.portlek.configs;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public class BukkitLinkedManaged extends BukkitManaged implements LnkdFlManaged {

    @SafeVarargs
    public BukkitLinkedManaged(@NotNull final String chosen,
                               @NotNull final Map.Entry<String, Object>... objects) {
        super(new LinkedFileManaged(chosen), objects);
    }

    @NotNull
    @Override
    public final <T> T match(@NotNull final Function<String, Optional<T>> function) {
        return ((LnkdFlManaged) this.getBase()).match(function);
    }

    @NotNull
    @Override
    public final String getChosen() {
        return ((LnkdFlManaged) this.getBase()).getChosen();
    }

}
