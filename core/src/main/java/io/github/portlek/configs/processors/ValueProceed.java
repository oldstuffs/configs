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

package io.github.portlek.configs.processors;

import io.github.portlek.configs.Managed;
import io.github.portlek.configs.Proceed;
import io.github.portlek.configs.annotations.Value;
import io.github.portlek.configs.util.PathCalc;
import io.github.portlek.configs.util.Replaceable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public final class ValueProceed implements Proceed<Field> {

    @NotNull
    private final Managed managed;

    @NotNull
    private final Object instance;

    @NotNull
    private final String parent;

    @NotNull
    private final Value value;

    public ValueProceed(@NotNull Managed managed, @NotNull Object instance, @NotNull String parent,
                        @NotNull Value value) {
        this.managed = managed;
        this.instance = instance;
        this.parent = parent;
        this.value = value;
    }

    @Override
    public void load(@NotNull Field field) throws Exception {
        final String path = new PathCalc(
            value.regex(),
            value.separator(),
            value.path(),
            parent,
            field.getName()
        ).value();
        final Optional<Object> defaultValueOptional = Optional.ofNullable(field.get(instance));

        if (!defaultValueOptional.isPresent()) {
            return;
        }

        final Object fieldValue = defaultValueOptional.get();
        final Optional<?> fileValueOptional = get(fieldValue, path);

        if (fileValueOptional.isPresent()) {
            field.set(instance, fileValueOptional.get());
        } else {
            managed.set(path, set(fieldValue));
        }
    }

    @SuppressWarnings("unchecked")
    @NotNull
    private Optional<?> get(@NotNull Object fieldValue, @NotNull String path) {
        if (fieldValue instanceof String) {
            return managed.getString(path);
        } else if (fieldValue instanceof List) {
            return Optional.of(managed.getList(path));
        } else if (fieldValue instanceof Replaceable<?>) {
            final Replaceable<?> replaceable = (Replaceable<?>) fieldValue;

            if (replaceable.getValue() instanceof String) {
                final Optional<String> stringOptional = managed.getString(path);
                final Replaceable<String> genericReplaceable = (Replaceable<String>) replaceable;

                if (stringOptional.isPresent()) {
                    return Optional.of(
                        Replaceable.of(stringOptional.get())
                            .replaces(replaceable.getRegex())
                            .replace(replaceable.getReplaces())
                            .map(genericReplaceable.getMaps())
                    );
                }
            } else if (replaceable.getValue() instanceof List<?>) {
                final List<String> list = (List<String>) managed.getList(path);
                final Replaceable<List<String>> genericReplaceable = (Replaceable<List<String>>) replaceable;

                return Optional.of(
                    Replaceable.of(list)
                        .replaces(genericReplaceable.getRegex())
                        .replace(genericReplaceable.getReplaces())
                        .map(genericReplaceable.getMaps())
                );
            }
        }

        return managed.get(path);
    }

    @NotNull
    private Object set(@NotNull Object fieldValue) {
        if (fieldValue instanceof Replaceable<?>) {
            return ((Replaceable<?>) fieldValue).getValue();
        }

        return fieldValue;
    }

}
