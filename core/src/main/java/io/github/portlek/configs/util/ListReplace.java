package io.github.portlek.configs.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class ListReplace {

    @NotNull
    private final List<String> list;

    public ListReplace(@NotNull List<String> list) {
        this.list = list;
    }

    @NotNull
    public List<String> apply(@NotNull String regex, @NotNull String replace) {
        final List<String> finalList = new ArrayList<>();

        list.forEach(s ->
            finalList.add(s.replace(regex, replace))
        );

        return finalList;
    }

}
