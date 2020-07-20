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

import io.github.portlek.configs.Replaceable;
import java.util.*;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class ReplaceableEnvelope<S extends ReplaceableEnvelope<S, X>, X> implements Replaceable<S, X> {

    @NotNull
    private final Collection<String> regex = new ArrayList<>();

    @NotNull
    private final Map<String, Supplier<String>> replaces = new HashMap<>();

    @NotNull
    private final Collection<UnaryOperator<X>> maps = new ArrayList<>();

    @NotNull
    private final X value;

    @NotNull
    @Override
    public final S replace(@NotNull final Map<String, Supplier<String>> replaces) {
        this.replaces.putAll(replaces);
        return this.self();
    }

    @SafeVarargs
    @NotNull
    @Override
    public final S replace(@NotNull final Map.Entry<String, Supplier<String>>... replaces) {
        this.replaces.putAll(Arrays.stream(replaces)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        return this.self();
    }

    @NotNull
    @Override
    public final S replaces(@NotNull final Collection<String> regex) {
        this.regex.addAll(regex);
        return this.self();
    }

    @NotNull
    @Override
    public final S map(@NotNull final Collection<UnaryOperator<X>> map) {
        this.maps.addAll(map);
        return this.self();
    }

    @NotNull
    @Override
    public final X getValue() {
        return this.value;
    }

    @NotNull
    @Override
    public final Collection<String> getRegex() {
        return Collections.unmodifiableCollection(this.regex);
    }

    @NotNull
    @Override
    public final Map<String, Supplier<String>> getReplaces() {
        return Collections.unmodifiableMap(this.replaces);
    }

    @NotNull
    @Override
    public final Collection<UnaryOperator<X>> getMaps() {
        return Collections.unmodifiableCollection(this.maps);
    }

}
