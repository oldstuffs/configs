package io.github.portlek.configs.util;

import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class ListToString {

    @NotNull
    private final List<String> list;

    public ListToString(@NotNull List<String> list) {
        this.list = list;
    }

    public ListToString(@NotNull String... array) {
        this(Arrays.asList(array));
    }

    @NotNull
    public String value() {
        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i));

            if (i < list.size() - 1) {
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }

}
