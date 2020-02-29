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

import io.github.portlek.configs.Proceed;
import io.github.portlek.configs.annotations.Value;
import io.github.portlek.configs.util.PathCalc;
import io.github.portlek.configs.util.Replaceable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import org.jetbrains.annotations.NotNull;

public final class ValueProceed implements Proceed<Field> {

    @NotNull
    private final Managed managed;

    @NotNull
    private final Object instance;

    @NotNull
    private final String parent;

    @NotNull
    private final Value value;

    @NotNull
    private final BiFunction<Object, String, Optional<?>> get;

    @NotNull
    private final BiPredicate<Object, String> set;

    public ValueProceed(@NotNull final Managed managed, @NotNull final Object instance, @NotNull final String parent,
                        @NotNull final Value value, @NotNull final BiFunction<Object, String, Optional<?>> get,
                        @NotNull final BiPredicate<Object, String> set) {
        this.managed = managed;
        this.instance = instance;
        this.parent = parent;
        this.value = value;
        this.get = get;
        this.set = set;
    }

    @Override
    public void load(@NotNull final Field field) throws Exception {
        final String path = new PathCalc(
            this.value.regex(),
            this.value.separator(),
            this.value.path(),
            this.parent,
            field.getName()
        ).value();
        final Optional<Object> defaultValueOptional = Optional.ofNullable(field.get(this.instance));

        if (!defaultValueOptional.isPresent()) {
            return;
        }

        final Object fieldValue = defaultValueOptional.get();
        final Optional<?> fileValueOptional = this.get(fieldValue, path);

        if (fileValueOptional.isPresent()) {
            field.set(this.instance, fileValueOptional.get());
        } else {
            this.set(fieldValue, path);
        }
    }

    @SuppressWarnings("unchecked")
    @NotNull
    private Optional<?> get(@NotNull final Object fieldValue, @NotNull final String path) {
        final Optional<Provided<?>> customValueOptional = this.managed.getCustomValue(fieldValue.getClass());

        if (customValueOptional.isPresent()) {
            return customValueOptional.get().get(this.managed, path);
        }

        if (fieldValue instanceof String) {
            return this.managed.getString(path);
        } else if (fieldValue instanceof List) {
            return this.managed.getList(path);
        } else if (fieldValue instanceof Replaceable<?>) {
            final Replaceable<?> replaceable = (Replaceable<?>) fieldValue;

            if (replaceable.getValue() instanceof String) {
                final Optional<String> stringOptional = this.managed.getString(path);
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
                final Optional<List<?>> listOptional = this.managed.getList(path);

                if (listOptional.isPresent()) {
                    final Replaceable<List<String>> genericReplaceable = (Replaceable<List<String>>) replaceable;

                    return Optional.of(
                        Replaceable.of((List<String>) listOptional.get())
                            .replaces(genericReplaceable.getRegex())
                            .replace(genericReplaceable.getReplaces())
                            .map(genericReplaceable.getMaps())
                    );
                }
            }
        }

        final Optional<?> optional = this.get.apply(fieldValue, path);

        if (optional.isPresent()) {
            return optional;
        }

        return this.managed.get(path);
    }

    private void set(@NotNull final Object fieldValue, @NotNull final String path) {
        final Optional<Provided<?>> customValueOptional = this.managed.getCustomValue(fieldValue.getClass());

        if (customValueOptional.isPresent()) {
            customValueOptional.get().set(fieldValue, this.managed, path);
            return;
        }

        if (fieldValue instanceof Replaceable) {
            this.managed.set(path, ((Replaceable<?>) fieldValue).getValue());
            return;
        }

        if (!this.set.test(fieldValue, path)) {
            this.managed.set(path, fieldValue);
        }
    }

}
