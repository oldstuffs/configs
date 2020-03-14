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
import io.github.portlek.configs.Provided;
import io.github.portlek.configs.annotations.Value;
import io.github.portlek.configs.util.PathCalc;
import io.github.portlek.configs.util.Replaceable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
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

    public ValueProceed(@NotNull final Managed mngd, @NotNull final Object instnce, @NotNull final String prnt,
                        @NotNull final Value vlue) {
        this.managed = mngd;
        this.instance = instnce;
        this.parent = prnt;
        this.value = vlue;
    }

    @Override
    public void load(@NotNull final Field field) {
        final String path = new PathCalc(
            this.value.regex(),
            this.value.separator(),
            this.value.path(),
            this.parent,
            field.getName()
        ).value();
        try {
            final Optional<Object> optional = Optional.ofNullable(field.get(this.instance));
            if (!optional.isPresent()) {
                return;
            }
            final Object fieldvalue = optional.get();
            final Optional<?> filevalueoptional = this.get(fieldvalue, path);

            if (filevalueoptional.isPresent()) {
                field.set(this.instance, filevalueoptional.get());
            } else {
                this.set(fieldvalue, path);
            }
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @NotNull
    private Optional<?> get(@NotNull final Object fieldvalue, @NotNull final String path) {
        final Optional<Provided<?>> optional = this.managed.getCustomValue(fieldvalue.getClass());
        if (optional.isPresent()) {
            return optional.get().get(this.managed, path);
        }
        if (fieldvalue instanceof String) {
            return this.managed.getString(path);
        }
        if (fieldvalue instanceof List) {
            return this.managed.getList(path);
        }
        if (fieldvalue instanceof Replaceable<?>) {
            final Replaceable<?> replaceable = (Replaceable<?>) fieldvalue;
            if (replaceable.getValue() instanceof String) {
                final Optional<String> optionalstring = this.managed.getString(path);
                final Replaceable<String> genericreplaceable = (Replaceable<String>) replaceable;
                if (optionalstring.isPresent()) {
                    return Optional.of(
                        Replaceable.of(optionalstring.get())
                            .replaces(replaceable.getRegex())
                            .replace(replaceable.getReplaces())
                            .map(genericreplaceable.getMaps())
                    );
                }
            } else if (replaceable.getValue() instanceof List<?>) {
                final Optional<List<?>> listoptional = this.managed.getList(path);
                if (listoptional.isPresent()) {
                    final Replaceable<List<String>> genericreplaceable = (Replaceable<List<String>>) replaceable;
                    return Optional.of(
                        Replaceable.of((List<String>) listoptional.get())
                            .replaces(genericreplaceable.getRegex())
                            .replace(genericreplaceable.getReplaces())
                            .map(genericreplaceable.getMaps())
                    );
                }
            }
        }
        return this.managed.get(path);
    }

    private void set(@NotNull final Object fieldValue, @NotNull final String path) {
        final Optional<Provided<?>> optionalProvided = this.managed.getCustomValue(fieldValue.getClass());
        if (optionalProvided.isPresent()) {
            optionalProvided.get().set(fieldValue, this.managed, path);
            return;
        }
        if (fieldValue instanceof Replaceable<?>) {
            this.managed.set(path, ((Replaceable<?>) fieldValue).getValue());
            return;
        }
        this.managed.set(path, fieldValue);
    }

}
