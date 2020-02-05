package io.github.portlek.configs.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class Replaceable<X> {

    @NotNull
    private final X value;

    @NotNull
    private final List<String> regex = new ArrayList<>();

    @NotNull
    private final Map<String, Supplier<String>> replaces = new HashMap<>();

    @NotNull
    private final List<UnaryOperator<X>> maps = new ArrayList<>();

    private Replaceable(@NotNull X value) {
        this.value = value;
    }

    @NotNull
    public Replaceable<X> replace(@NotNull String regex, @NotNull Supplier<String> replace) {
        return replace(Collections.singletonMap(regex, replace));
    }

    @NotNull
    public Replaceable<X> replace(@NotNull Map<String, Supplier<String>> replaces) {
        this.replaces.putAll(replaces);

        return this;
    }

    @NotNull
    public Replaceable<X> replaces(@NotNull String... regex) {
        return replaces(Arrays.asList(regex));
    }

    @NotNull
    public Replaceable<X> replaces(@NotNull List<String> regex) {
        this.regex.addAll(regex);

        return this;
    }

    @NotNull
    public Replaceable<X> map(@NotNull UnaryOperator<X> map) {
        return map(Collections.singletonList(map));
    }

    @NotNull
    public Replaceable<X> map(@NotNull List<UnaryOperator<X>> map) {
        this.maps.addAll(map);

        return this;
    }

    @NotNull
    public X build(@NotNull String regex, @NotNull Supplier<String> replace) {
        return build(
            MapEntry.of(regex, replace)
        );
    }

    @SafeVarargs
    @NotNull
    public final X build(@NotNull Map.Entry<String, Supplier<String>>... entries) {
        return build(
            Arrays.asList(entries)
        );
    }

    @NotNull
    public X build(@NotNull List<Map.Entry<String, Supplier<String>>> entries) {
        final Map<String, Supplier<String>> map = new HashMap<>();

        entries.forEach(entry ->
            map.put(entry.getKey(), entry.getValue())
        );

        return build(map);
    }

    @NotNull
    public <Y> Y buildMap(@NotNull Function<X, Y> function) {
        final X built = build();

        return function.apply(built);
    }

    @NotNull
    public X build() {
        return build(
            Collections.emptyMap()
        );
    }

    @NotNull
    public <Y> Y buildMap(@NotNull Function<X, Y> function, @NotNull Map<String, Supplier<String>> replaces) {
        final X built = build(replaces);

        return function.apply(built);
    }

    @NotNull
    public X build(@NotNull Map<String, Supplier<String>> replaces) {
        final AtomicReference<X> finalValue = new AtomicReference<>(value);

        this.replaces.forEach((s, replace) ->
            replace(finalValue, s, replace.get())
        );
        regex.forEach(r ->
            Optional.ofNullable(replaces.get(r)).ifPresent(s ->
                replace(finalValue, r, s.get())
            )
        );
        maps.forEach(operator ->
            finalValue.set(operator.apply(finalValue.get()))
        );

        return finalValue.get();
    }

    @NotNull
    public X getValue() {
        return value;
    }

    @NotNull
    public List<String> getRegex() {
        return Collections.unmodifiableList(regex);
    }

    @NotNull
    public Map<String, Supplier<String>> getReplaces() {
        return Collections.unmodifiableMap(replaces);
    }

    @NotNull
    public List<UnaryOperator<X>> getMaps() {
        return Collections.unmodifiableList(maps);
    }

    @NotNull
    public static Replaceable<String> of(@NotNull String text) {
        return new Replaceable<>(text);
    }

    @NotNull
    public static Replaceable<List<String>> of(@NotNull String... texts) {
        return of(Arrays.asList(texts));
    }

    @NotNull
    public static Replaceable<List<String>> of(@NotNull List<String> list) {
        return new Replaceable<>(list);
    }

    @SuppressWarnings("unchecked")
    private void replace(@NotNull AtomicReference<X> finalValue, @NotNull String regex, @NotNull String replace) {
        if (value instanceof String) {
            finalValue.set((X) ((String)finalValue.get()).replace(regex, replace));
        } else if (value instanceof List<?>) {
            finalValue.set((X) new ListReplace(((List<String>)finalValue.get())).apply(regex, replace));
        }
    }

}
