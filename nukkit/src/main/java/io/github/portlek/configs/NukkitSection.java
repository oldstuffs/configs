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

package io.github.portlek.configs;

import io.github.portlek.configs.files.configuration.ConfigurationSection;
import io.github.portlek.configs.structure.managed.FlManaged;
import io.github.portlek.configs.structure.managed.section.CfgSection;
import io.github.portlek.configs.structure.managed.section.ConfigSection;
import org.jetbrains.annotations.NotNull;

public class NukkitSection implements NkktSection {

    @NotNull
    private final CfgSection base;

    public NukkitSection() {
        this(new ConfigSection());
    }

    public NukkitSection(@NotNull final CfgSection base) {
        this.base = base;
    }

    @NotNull
    @Override
    public CfgSection getBase() {
        return this.base;
    }

    @NotNull
    @Override
    public ConfigurationSection getConfigurationSection() {
        return this.getBase().getConfigurationSection();
    }

    @Override
    @NotNull
    public final FlManaged getManaged() {
        return this.getBase().getManaged();
    }

    @Override
    public final void setup(@NotNull final FlManaged managed, @NotNull final ConfigurationSection section) {
        this.getBase().setup(managed, section);
    }

}
