package io.github.portlek.configs.util;

import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public final class ListReplace {

    @NotNull
    private final List<String> list;

    public ListReplace(@NotNull final List<String> strngs) {
        this.list = strngs;
    }

    @NotNull
    public List<String> apply(@NotNull final CharSequence rgx, @NotNull final CharSequence rplce) {
        return this.list.stream()
            .map(s -> s.replace(rgx, rplce))
            .collect(Collectors.toList());
    }

}
