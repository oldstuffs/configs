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
import io.github.portlek.configs.Provided;
import io.github.portlek.configs.ProvidedSet;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.util.GeneralUtilities;
import io.github.portlek.reflection.RefField;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class PropertyProceed {

    @NotNull
    private final Property property;

    @NotNull
    private final Object parentObject;

    @NotNull
    private final CfgSection parent;

    @NotNull
    private final RefField field;

    public PropertyProceed(@NotNull final CfgSection parent, @NotNull final Property property, @NotNull final RefField field) {
        this(property, parent, parent, field);
    }

    @NotNull
    public static Optional<Object> get(@NotNull final CfgSection parent, @NotNull final Object fieldvalue,
                                       @NotNull final String path) {
        // noinspection unchecked
        final Class<Object> aClass = (Class<Object>) fieldvalue.getClass();
        return CfgSection.getProvidedClass(aClass)
            .map(objectProvided -> objectProvided.getWithField(fieldvalue, parent, path))
            .orElseGet(() ->
                CfgSection.getProvidedGetMethod(aClass)
                    .map(func -> func.getWithField(fieldvalue, parent, path))
                    .orElseGet(() -> parent.get(path)));
    }

    @NotNull
    public static Optional<Object> get(@NotNull final CfgSection parent, @NotNull final Class<Object> fieldClass,
                                       @NotNull final String path) {
        return CfgSection.getProvidedClass(fieldClass)
            .map(objectProvided -> objectProvided.get(parent, path))
            .orElseGet(() ->
                CfgSection.getProvidedGetMethod(fieldClass)
                    .map(func -> func.get(parent, path))
                    .orElseGet(() -> parent.get(path)));
    }

    public static void set(@NotNull final CfgSection parent, @NotNull final Object fieldValue,
                           @NotNull final String path) {
        //noinspection unchecked
        final Class<Object> clazz = (Class<Object>) fieldValue.getClass();
        final Optional<Provided<Object>> optional = CfgSection.getProvidedClass(clazz);
        if (optional.isPresent()) {
            optional.get().set(fieldValue, parent, path);
            return;
        }
        final Optional<ProvidedSet<Object>> setoptional = CfgSection.getProvidedSetMethod(clazz);
        if (setoptional.isPresent()) {
            setoptional.get().set(fieldValue, parent, path);
        } else {
            parent.set(path, fieldValue);
        }
    }

    @SneakyThrows
    public void load() {
        final Optional<Object> optional = this.field.of(this.parentObject).get();
        if (!optional.isPresent()) {
            return;
        }
        final String path = GeneralUtilities.calculatePath(
            this.property.regex(),
            this.property.separator(),
            this.property.value(),
            this.field.name());
        final Object fieldvalue = optional.get();
        final Optional<Object> filevalueoptional = PropertyProceed.get(this.parent, fieldvalue, path);
        if (filevalueoptional.isPresent()) {
            this.field.of(this.parentObject).set(filevalueoptional.get());
        } else {
            PropertyProceed.set(this.parent, fieldvalue, path);
        }
    }

}
