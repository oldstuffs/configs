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

package io.github.portlek.configs.configuration;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MemoryConfiguration extends MemorySection implements Configuration {

    protected Configuration defaults;

    protected MemoryConfigurationOptions options;

    public MemoryConfiguration() {
    }

    public MemoryConfiguration(@Nullable final Configuration defaults) {
        this.defaults = defaults;
    }

    @Nullable
    @Override
    public final ConfigurationSection getParent() {
        return null;
    }

    @Override
    public final void addDefault(@NotNull final String path, @Nullable final Object value) {
        if (this.defaults == null) {
            this.defaults = new MemoryConfiguration();
        }
        this.defaults.set(path, value);
    }

    @Override
    public final void addDefaults(@NotNull final Map<String, Object> defaults) {
        for (final Map.Entry<String, Object> entry : defaults.entrySet()) {
            this.addDefault(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public final void addDefaults(@NotNull final Configuration defaults) {
        this.addDefaults(defaults.getValues(true));
    }

    @Override
    @Nullable
    public final Configuration getDefaults() {
        return this.defaults;
    }

    @Override
    public final void setDefaults(@NotNull final Configuration defaults) {
        this.defaults = defaults;
    }

    @Override
    @NotNull
    public MemoryConfigurationOptions options() {
        if (this.options == null) {
            this.options = new MemoryConfigurationOptions(this);
        }

        return this.options;
    }

}
