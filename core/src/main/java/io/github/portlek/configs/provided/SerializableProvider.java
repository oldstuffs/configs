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

import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.processors.PropertyProceed;
import io.github.portlek.configs.structure.managed.section.CfgSection;
import io.github.portlek.configs.util.GeneralUtilities;
import io.github.portlek.configs.util.MapEntry;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public final class SerializableProvider<T> implements Provided<T> {

    private static final Field[] FIELDS = new Field[0];

    @NotNull
    private final Class<T> tClass;

    @NotNull
    private Map.Entry<Field, Property>[] fieldAnnotatedProperty = new Map.Entry[0];

    @Nullable
    private Constructor<T> constructor;

    @SneakyThrows
    public void initiate() {
        final Field[] fields = this.parseFields(this.tClass).toArray(SerializableProvider.FIELDS);
        final AtomicInteger size = new AtomicInteger(0);
        Arrays.stream(fields)
            .map(field -> Optional.ofNullable(field.getDeclaredAnnotation(Property.class)))
            .filter(Optional::isPresent)
            .forEach(property -> size.incrementAndGet());
        this.fieldAnnotatedProperty = new Map.Entry[size.get()];
        for (int index = 0; index < this.fieldAnnotatedProperty.length; index++) {
            final Field field = fields[index];
            final int finalindex = index;
            Optional.ofNullable(field.getDeclaredAnnotation(Property.class)).ifPresent(property ->
                this.fieldAnnotatedProperty[finalindex] = MapEntry.from(field, property));
        }
        for (final Constructor<?> cons : this.tClass.getDeclaredConstructors()) {
            if (cons.getParameterTypes().length == this.fieldAnnotatedProperty.length) {
                // noinspection unchecked
                this.constructor = (Constructor<T>) cons;
            }
        }
    }

    @SneakyThrows
    @Override
    public void set(@NotNull final T t, @NotNull final CfgSection section, @NotNull final String path) {
        if (!Optional.ofNullable(this.fieldAnnotatedProperty).isPresent()) {
            return;
        }
        final CfgSection finalsection;
        if (path.isEmpty()) {
            finalsection = section;
        } else {
            finalsection = section.getOrCreateSection(path);
        }
        for (final Map.Entry<Field, Property> entry : this.fieldAnnotatedProperty) {
            final Property property = entry.getValue();
            final Field field = entry.getKey();
            final String fieldpath = GeneralUtilities.calculatePath(
                property.regex(),
                property.separator(),
                property.value(),
                field.getName());
            final boolean accessible = field.isAccessible();
            field.setAccessible(true);
            PropertyProceed.set(finalsection, field.get(t), fieldpath);
            field.setAccessible(accessible);
        }
    }

    @SneakyThrows
    @NotNull
    @Override
    public Optional<T> get(@NotNull final CfgSection section, @NotNull final String path) {
        if (!Optional.ofNullable(this.constructor).isPresent()) {
            return Optional.empty();
        }
        final CfgSection finalsection;
        if (path.isEmpty()) {
            finalsection = section;
        } else {
            finalsection = section.getOrCreateSection(path);
        }
        final Optional<?>[] clone = new Optional[this.constructor.getParameterTypes().length];
        for (int index = 0; index < clone.length && index < this.fieldAnnotatedProperty.length; index++) {
            final Map.Entry<Field, Property> entry = this.fieldAnnotatedProperty[index];
            final Property property = entry.getValue();
            final Field field = entry.getKey();
            final String fieldpath = GeneralUtilities.calculatePath(
                property.regex(),
                property.separator(),
                property.value(),
                field.getName());
            final boolean accessible = field.isAccessible();
            field.setAccessible(true);
            // noinspection unchecked
            clone[index] = PropertyProceed.get(finalsection, (Class<Object>) field.getType(), fieldpath);
            field.setAccessible(accessible);
        }
        final Object[] objects = new Object[clone.length];
        for (int index = 0; index < clone.length; index++) {
            final Optional<?> optional = clone[index];
            if (!optional.isPresent()) {
                return Optional.empty();
            }
            objects[index] = optional.get();
        }
        return Optional.of(this.constructor.newInstance(objects));
    }

    @NotNull
    private List<Field> parseFields(final Class<?> clazz) {
        final List<Field> fields = new ArrayList<>();
        if (!clazz.getSuperclass().equals(Object.class)) {
            fields.addAll(this.parseFields(clazz.getSuperclass()));
        }
        fields.addAll(Arrays.stream(clazz.getDeclaredFields())
            .filter(field -> Optional.ofNullable(field.getDeclaredAnnotation(Property.class)).isPresent())
            .collect(Collectors.toList()));
        return fields;
    }

}
