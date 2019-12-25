package io.github.portlek.configs;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface Replaceable {

    @NotNull
    Replaceable replace(@NotNull String regex, @NotNull Object replace);

    @NotNull
    Replaceable replace(@NotNull List<Map.Entry<@NotNull String, @NotNull Object>> replaces);

    @NotNull
    Replaceable replace(@NotNull Map<@NotNull String, @NotNull Object> replaces);

    @NotNull
    Replaceable clearReplaces();

    @NotNull
    String build();

}
