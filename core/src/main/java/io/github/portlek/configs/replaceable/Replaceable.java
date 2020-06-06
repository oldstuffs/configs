/*
 * MIT License
 *
 * Copyright (c) 2020 Hasan Demirtaş
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package io.github.portlek.configs.replaceable;

import io.github.portlek.configs.util.MapEntry;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.NotNull;

public interface Replaceable<S extends Replaceable<?, ?>, X> {

    @NotNull
    static ReplaceableString from(@NotNull final StringBuilder builder) {
        return Replaceable.from(builder.toString());
    }

    @NotNull
    static ReplaceableList from(@NotNull final String... texts) {
        return Replaceable.from(Arrays.asList(texts));
    }

    @NotNull
    static ReplaceableList from(@NotNull final List<String> list) {
        return new ReplaceableList(list);
    }

    @NotNull
    static ReplaceableString from(@NotNull final String text) {
        return new ReplaceableString(text);
    }

    @NotNull
    default S value(@NotNull final X value) {
        return this.newSelf(value).get()
            .replaces(this.getRegex())
            .replace(this.getReplaces())
            .map(this.getMaps());
    }

    @NotNull
    default <Y> Y buildMap(@NotNull final Function<X, Y> function,
                           @NotNull final Map<String, Supplier<String>> replaces) {
        return function.apply(this.build(replaces));
    }

    @NotNull
    default X build() {
        return this.build(Collections.emptyMap());
    }

    @NotNull
    default <Y> Y buildMap(@NotNull final Function<X, Y> function) {
        final X built = this.build();
        return function.apply(built);
    }

    @NotNull
    default X build(@NotNull final Map<String, Supplier<String>> replaces) {
        final AtomicReference<X> value = new AtomicReference<>(this.getValue());
        this.getReplaces().forEach((s, replace) ->
            this.replace(value, s, replace.get()));
        this.getRegex().forEach(r ->
            Optional.ofNullable(replaces.get(r)).ifPresent(supplier ->
                this.replace(value, r, supplier.get())));
        this.getMaps().forEach(operator ->
            value.set(operator.apply(value.get())));
        return value.get();
    }

    @NotNull
    default X build(@NotNull final Iterable<Map.Entry<String, Supplier<String>>> entries) {
        final Map<String, Supplier<String>> map = new HashMap<>();
        entries.forEach(entry ->
            map.put(entry.getKey(), entry.getValue()));
        return this.build(map);
    }

    @NotNull
    default X build(@NotNull final Map.Entry<String, Supplier<String>>... entries) {
        return this.build(Arrays.asList(entries));
    }

    @NotNull
    default X build(@NotNull final String regex, @NotNull final Supplier<String> replace) {
        return this.build(MapEntry.from(regex, replace));
    }

    @NotNull
    default S map(@NotNull final UnaryOperator<X> map) {
        return this.map(Collections.singletonList(map));
    }

    @NotNull
    default S replaces(@NotNull final String... regex) {
        return this.replaces(Arrays.asList(regex));
    }

    @NotNull
    default S replace(@NotNull final String regex, @NotNull final Supplier<String> replace) {
        return this.replace(Collections.singletonMap(regex, replace));
    }

    @NotNull
    S self();

    @NotNull
    Supplier<S> newSelf(@NotNull X value);

    void replace(@NotNull AtomicReference<X> value, @NotNull CharSequence regex, @NotNull CharSequence replace);

    S replace(@NotNull Map<String, Supplier<String>> replaces);

    @NotNull
    S replace(@NotNull Map.Entry<String, Supplier<String>>... replaces);

    @NotNull
    S replaces(@NotNull Collection<String> regex);

    @NotNull
    S map(@NotNull Collection<UnaryOperator<X>> map);

    @NotNull
    X getValue();

    @NotNull
    List<String> getRegex();

    @NotNull
    Map<String, Supplier<String>> getReplaces();

    @NotNull
    List<UnaryOperator<X>> getMaps();

}
