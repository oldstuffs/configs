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
import io.github.portlek.configs.annotations.Section;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import org.jetbrains.annotations.NotNull;

public final class InstanceProceed implements Proceed<Field> {

    @NotNull
    private final Managed managed;

    @NotNull
    private final Object object;

    @NotNull
    private final String parent;

    @NotNull
    private final BiFunction<Object, String, Optional<?>> get;

    @NotNull
    private final BiPredicate<Object, String> set;

    public InstanceProceed(@NotNull Managed managed, @NotNull Object object, @NotNull String parent,
        @NotNull BiFunction<Object, String, Optional<?>> get, @NotNull BiPredicate<Object, String> set) {
        this.managed = managed;
        this.object = object;
        this.parent = parent;
        this.get = get;
        this.set = set;
    }

    @Override
    public void load(@NotNull Field field) throws Exception {
        final Optional<Object> fieldObjectOptional = Optional.ofNullable(field.get(object));

        if (fieldObjectOptional.isPresent()) {
            final Optional<Section> sectionOptional = Optional.ofNullable(
                fieldObjectOptional.get().getClass().getDeclaredAnnotation(Section.class)
            );

            if (sectionOptional.isPresent()) {
                new SectionProceed(
                    managed,
                    parent,
                    sectionOptional.get(),
                    get,
                    set
                ).load(fieldObjectOptional.get());
            }
        }
    }

}
