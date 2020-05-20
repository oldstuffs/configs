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

package io.github.portlek.configs.structure.managed.section;

import io.github.portlek.configs.files.configuration.ConfigurationSection;
import io.github.portlek.configs.structure.managed.FlManaged;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigSection implements CfgSection {

    @Nullable
    private ConfigurationSection section;

    @Nullable
    private FlManaged managed;

    @NotNull
    @Override
    public final CfgSection getBase() {
        return this;
    }

    @NotNull
    @Override
    public ConfigurationSection getConfigurationSection() {
        return Objects.requireNonNull(this.section, "You have to load your class with '#load()' method");
    }

    @Override
    @NotNull
    public final FlManaged getManaged() {
        return Objects.requireNonNull(this.managed, "You have to load your class with '#load()' method");
    }

    @Override
    public final void setup(@NotNull final FlManaged managed, @NotNull final ConfigurationSection configurationSection) {
        this.section = configurationSection;
        this.managed = managed;
    }

}