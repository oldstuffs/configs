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

import io.github.portlek.configs.ConfigSection;
import io.github.portlek.configs.Managed;
import io.github.portlek.configs.Proceed;
import io.github.portlek.configs.annotations.Section;
import java.lang.reflect.Field;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public final class InstanceProceed implements Proceed<Field> {

    @NotNull
    private final Managed managed;

    @NotNull
    private final ConfigSection configSection;

    @NotNull
    private final String parent;

    public InstanceProceed(@NotNull final Managed managed, @NotNull final ConfigSection configSection,
                           @NotNull final String parent) {
        this.managed = managed;
        this.configSection = configSection;
        this.parent = parent;
    }

    @Override
    public void load(@NotNull final Field field) {
        try {
            Optional.ofNullable(field.get(this.configSection)).ifPresent(o ->
                Optional.ofNullable(o.getClass().getDeclaredAnnotation(Section.class)).ifPresent(section -> {
                    new SectionProceed(
                        this.managed,
                        this.parent,
                        section
                    ).load((ConfigSection) o);
                })
            );
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
