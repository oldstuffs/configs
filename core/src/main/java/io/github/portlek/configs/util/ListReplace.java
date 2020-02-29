package io.github.portlek.configs.util;

import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public final class ListReplace {

    @NotNull
    private final List<String> list;

    public ListReplace(@NotNull final List<String> lst) {
        this.list = lst;
    }

    @NotNull
    public List<String> apply(@NotNull final CharSequence regex, @NotNull final CharSequence replace) {
        return this.list.stream()
            .map(s -> s.replace(regex, replace))
            .collect(Collectors.toList());
    }

}
