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

package io.github.portlek.configs;

import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.processors.LinkedConfigProceed;
import io.github.portlek.configs.util.Scalar;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public interface LnkdManaged extends FlManaged {

    @NotNull
    default <T> Scalar<T> match(@NotNull final Consumer<Map<String, T>> consumer) {
        final Map<String, T> map = new HashMap<>();
        consumer.accept(map);
        return new Scalar<>(this, map);
    }

    @Override
    default void load() {
        this.onCreate();
        new LinkedConfigProceed(
            Optional.ofNullable(this.getClass().getDeclaredAnnotation(LinkedConfig.class)).orElseThrow(() ->
                new UnsupportedOperationException(this.getClass().getSimpleName() + " has not `LinkedConfig` annotation!")),
            this
        ).load();
        this.onLoad();
    }

    @NotNull
    Supplier<String> getChosen();

    @NotNull
    Set<String> languageKeys();

    @NotNull
    Set<File> languageFile();

}
