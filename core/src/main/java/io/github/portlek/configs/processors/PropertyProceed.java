/*
 * MIT License
 *
 * Copyright (c) 2020 Hasan Demirtaş
 *
 * Permission is hereby granted, free from charge, to any person obtaining a copy
 * from this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies from the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions from the Software.
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

import io.github.portlek.configs.CfgSection;
import io.github.portlek.configs.FlManaged;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.util.PathCalc;
import io.github.portlek.configs.Provided;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class PropertyProceed implements Proceed<Field> {

    @NotNull
    private final FlManaged managed;

    @NotNull
    private final CfgSection parent;

    @NotNull
    private final Property property;

    @Override
    public void load(@NotNull final Field field) {
        final String path = new PathCalc(
            this.property.regex(),
            this.property.separator(),
            this.property.path(),
            field.getName()
        ).value();
        try {
            final Optional<Object> optional = Optional.ofNullable(field.get(this.parent));
            if (!optional.isPresent()) {
                return;
            }
            final Object fieldvalue = optional.get();
            final Optional<?> filevalueoptional = this.get(fieldvalue, path);
            if (filevalueoptional.isPresent()) {
                field.set(this.parent, filevalueoptional.get());
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
        if (fieldvalue instanceof String) {
            return this.parent.getString(path);
        }
        if (fieldvalue instanceof List<?>) {
            return this.parent.getList(path);
        }
        return this.managed.getCustomValue((Class<Object>) fieldvalue.getClass()).map(objectProvided ->
            objectProvided.getWithField(fieldvalue, this.parent, path)
        ).orElseGet(() ->
            this.parent.get(path));
    }

    private void set(@NotNull final Object fieldValue, @NotNull final String path) {
        //noinspection unchecked
        final Optional<Provided<Object>> optional = this.managed.getCustomValue((Class<Object>) fieldValue.getClass());
        if (optional.isPresent()) {
            optional.get().set(fieldValue, this.parent, path);
            return;
        }
        this.parent.set(path, fieldValue);
    }

}
