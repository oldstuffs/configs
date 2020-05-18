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

package io.github.portlek.configs.provided;

import io.github.portlek.configs.CfgSection;
import io.github.portlek.configs.util.Replaceable;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public final class ReplaceableProvider implements Provided<Replaceable<?>> {

    @Override
    public void set(@NotNull final Replaceable<?> replaceable, @NotNull final CfgSection section,
                    @NotNull final String path) {
        section.set(path, replaceable.getValue());
    }

    @NotNull
    @Override
    public Optional<Replaceable<?>> getWithField(@NotNull final Replaceable<?> replaceable,
                                                 @NotNull final CfgSection section, @NotNull final String path) {
        if (replaceable.getValue() instanceof String) {
            final Optional<String> optionalstring = section.getString(path);
            final Replaceable<String> genericreplaceable = (Replaceable<String>) replaceable;
            if (optionalstring.isPresent()) {
                return Optional.of(
                    Replaceable.from(optionalstring.get())
                        .replaces(genericreplaceable.getRegex())
                        .replace(genericreplaceable.getReplaces())
                        .map(genericreplaceable.getMaps())
                );
            }
        } else if (replaceable.getValue() instanceof List<?>) {
            final Optional<List<?>> listoptional = section.getList(path);
            if (listoptional.isPresent()) {
                final Replaceable<List<String>> genericreplaceable = (Replaceable<List<String>>) replaceable;
                return Optional.of(
                    Replaceable.from((List<String>) listoptional.get())
                        .replaces(genericreplaceable.getRegex())
                        .replace(genericreplaceable.getReplaces())
                        .map(genericreplaceable.getMaps())
                );
            }
        }
        return Optional.empty();
    }

    @NotNull
    @Override
    public Optional<Replaceable<?>> get(@NotNull final CfgSection section, @NotNull final String path) {
        return Optional.empty();
    }

}