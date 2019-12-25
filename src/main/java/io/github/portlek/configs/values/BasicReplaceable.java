package io.github.portlek.configs.values;

import io.github.portlek.configs.Replaceable;
import org.cactoos.list.ListOf;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public final class BasicReplaceable implements Replaceable {

    @NotNull
    private final Map<String, Object> replaces = new HashMap<>();

    @NotNull
    private final String rawMessage;

    public BasicReplaceable(@NotNull String rawMessage) {
        this.rawMessage = rawMessage;
    }

    @NotNull
    @Override
    public Replaceable replace(@NotNull String regex, @NotNull Object replace) {
        return replace(
            new ListOf<>(
                new MapEntry<>(regex, replace)
            )
        );
    }

    @NotNull
    @Override
    public Replaceable replace(@NotNull List<Map.Entry<@NotNull String, @NotNull Object>> replaces) {
        return replace(
            new MapOf<>(replaces)
        );
    }

    @NotNull
    @Override
    public Replaceable replace(@NotNull Map<@NotNull String, @NotNull Object> replaces) {
        replaces.forEach(this.replaces::put);

        return this;
    }

    @NotNull
    @Override
    public Replaceable clearReplaces() {
        replaces.clear();

        return this;
    }

    @NotNull
    @Override
    public String build() {
        final AtomicReference<String> replaced = new AtomicReference<>(rawMessage);

        replaces
            .keySet()
            .stream()
            .filter(o -> !o.isEmpty())
            .forEach(o ->
                replaced.set(
                    replaced.get().replace(o, replaces.getOrDefault(o, "").toString())
                )
            );

        return replaced.get();
    }
}
