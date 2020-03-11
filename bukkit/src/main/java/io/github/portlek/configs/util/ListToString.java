package io.github.portlek.configs.util;

import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class ListToString {

    @NotNull
    private final List<String> list;

    public ListToString(@NotNull final String... array) {
        this(Arrays.asList(array));
    }

    public ListToString(@NotNull final List<String> lst) {
        this.list = lst;
    }

    @NotNull
    public String value() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.list.size(); i++) {
            builder.append(this.list.get(i));
            if (i < this.list.size() - 1) {
                builder.append('\n');
            }
        }
        return builder.toString();
    }

}
