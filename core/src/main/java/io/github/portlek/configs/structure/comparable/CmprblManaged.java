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

package io.github.portlek.configs.structure.comparable;

import io.github.portlek.configs.annotations.ComparableConfig;
import io.github.portlek.configs.configuration.FileConfiguration;
import io.github.portlek.configs.languageable.Languageable;
import io.github.portlek.configs.processors.ComparableConfigProceed;
import io.github.portlek.configs.structure.managed.FlManaged;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public interface CmprblManaged<S extends CmprblManaged<S>> extends FlManaged {

    @Override
    default void load() {
        this.onCreate();
        new ComparableConfigProceed<S>(
            Optional.ofNullable(this.getClass().getDeclaredAnnotation(ComparableConfig.class)).orElseThrow(() ->
                new UnsupportedOperationException(this.getClass().getSimpleName() + " has not `ComparableConfig` annotation!")),
            this
        ).load();
        this.onLoad();
    }

    @Override
    default void setup(@NotNull final File file, @NotNull final FileConfiguration section) {
    }

    default <T> Languageable<T> languageable(@NotNull final Class<T> tclass,
                                             @NotNull final Map.Entry<Object, T>... entries) {
        return this.languageable(tclass,Arrays.asList(entries));
    }

    default <T> Languageable<T> languageable(@NotNull final Class<T> tclass,
                                             @NotNull final List<Map.Entry<Object, T>> entries) {
        return this.languageable(tclass, entries.stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    default <T> Languageable<T> languageable(@NotNull final Class<T> tclass,
                                             @NotNull final Map<Object, T> entries) {
        return new Languageable<>(tclass, entries);
    }

    @NotNull
    S key(@NotNull String key) throws RuntimeException;

    void setup(@NotNull String key, @NotNull FlManaged managed);

    @NotNull
    S self();

}
