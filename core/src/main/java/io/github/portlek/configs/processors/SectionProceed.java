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
import io.github.portlek.configs.util.PathCalc;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;

public final class SectionProceed implements Proceed<Object> {

    @NotNull
    private final Managed managed;

    @NotNull
    private final String parent;

    @NotNull
    private final Section section;

    @NotNull
    private final BiFunction<Object, String, Optional<?>> get;

    @NotNull
    private final BiPredicate<Object, String> set;

    public SectionProceed(@NotNull Managed managed, @NotNull String parent, @NotNull Section section,
        @NotNull BiFunction<Object, String, Optional<?>> get,
        @NotNull BiPredicate<Object, String> set) {
        this.managed = managed;
        this.parent = parent;
        this.section = section;
        this.get = get;
        this.set = set;
    }

    @Override
    public void load(@NotNull Object object) throws Exception {
        final String path = new PathCalc(
            "",
            "",
            section.path(),
            parent,
            object.getClass().getName()
        ).value();
        final Optional<ConfigurationSection> configurationSectionOptional = managed.getSection(path);

        if (!configurationSectionOptional.isPresent()) {
            managed.createSection(path);
        }

        new FieldsProceed(object, path, get, set).load(managed);
    }

}
