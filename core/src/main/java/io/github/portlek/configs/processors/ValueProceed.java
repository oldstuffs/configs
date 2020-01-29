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
            managed.set(path, fieldValue);
        }
    }

    @NotNull
    private Optional<?> get(@NotNull Object fieldValue, @NotNull String path) {
        if (fieldValue instanceof String) {
            return managed.getString(path);
        } else if (fieldValue instanceof Integer) {
            return Optional.of(managed.getInt(path));
        } else if (fieldValue instanceof List) {
            return Optional.of(managed.getList(path));
        } else if (fieldValue instanceof Boolean) {
            return Optional.of(managed.getBoolean(path));
        } else if (fieldValue instanceof Long) {
            return Optional.of(managed.getLong(path));
        } else if (fieldValue instanceof Double) {
            return Optional.of(managed.getDouble(path));
        }

        return managed.get(path);
    }

}
