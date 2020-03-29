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

import io.github.portlek.configs.CfgSection;
import io.github.portlek.configs.FlManaged;
import io.github.portlek.configs.annotations.Section;
import org.jetbrains.annotations.NotNull;

public final class SectionProceed implements Proceed<CfgSection> {

    @NotNull
    private final FlManaged managed;

    @NotNull
    private final CfgSection parent;

    @NotNull
    private final Section section;

    public SectionProceed(@NotNull final FlManaged managed, @NotNull final CfgSection parent,
                          @NotNull final Section section) {
        this.managed = managed;
        this.parent = parent;
        this.section = section;
    }

    @Override
    public void load(@NotNull final CfgSection sctn) {
        sctn.setup(this.managed, this.parent.getOrCreateSection(this.section.path()).getConfigurationSection());
        new FieldsProceed(sctn).load(this.managed);
    }

}
