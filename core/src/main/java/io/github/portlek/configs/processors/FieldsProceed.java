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

import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.structure.section.CfgSection;
import io.github.portlek.reflection.RefClass;
import io.github.portlek.reflection.clazz.ClassOf;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class FieldsProceed {

    @NotNull
    private final Object parentObject;

    @NotNull
    private final CfgSection parent;

    public FieldsProceed(@NotNull final CfgSection parent) {
        this(parent, parent);
    }

    public void load() {
        final RefClass<Object> parentclass = new ClassOf<>(this.parentObject);
        parentclass
            .declaredFieldsWithAnnotation(Property.class, (refField, property) ->
                new PropertyProceed(property, this.parentObject, this.parent, refField).load());
        parentclass
            .declaredFieldsWithAnnotation(Instance.class, (refField, instance) ->
                refField.of(this.parentObject).get()
                    .filter(o -> CfgSection.class.isAssignableFrom(o.getClass()))
                    .map(o -> (CfgSection) o)
                    .ifPresent(initiatedCfgSection ->
                        new ClassOf<>(initiatedCfgSection).annotation(Section.class, section -> {
                            initiatedCfgSection.setup(this.parent.getParent(),
                                this.parent.getOrCreateSection(section.value()).getConfigurationSection());
                            new FieldsProceed(initiatedCfgSection).load();
                        })));
    }

}
