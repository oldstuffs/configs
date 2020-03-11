package io.github.portlek.configs.util;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class ListReplace {

    @NotNull
    private final List<String> list;

    public ListReplace(@NotNull final List<String> list) {
        this.list = list;
    }

    @NotNull
    public List<String> apply(@NotNull final String regex, @NotNull final String replace) {
        final List<String> finalList = new ArrayList<>();

        this.list.forEach(s ->
            finalList.add(s.replace(regex, replace))
        );

        return finalList;
    }

}
