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

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public final class ReplaceableList extends ReplaceableEnvelope<ReplaceableList, List<String>> {

    public ReplaceableList(@NotNull final List<String> value) {
        super(value);
    }

    @NotNull
    @Override
    public ReplaceableList self() {
        return this;
    }

    @NotNull
    @Override
    public Supplier<ReplaceableList> newSelf(@NotNull final List<String> value) {
        return () -> new ReplaceableList(value);
    }

    @Override
    public void replace(@NotNull final AtomicReference<List<String>> value, @NotNull final CharSequence regex,
                        @NotNull final CharSequence replace) {
        value.set(value.get().stream()
            .map(s -> s.replace(regex, replace))
            .collect(Collectors.toList()));
    }

}
