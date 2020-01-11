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

package io.github.portlek.configs.configuration;

import io.github.portlek.configs.configuration.mock.MckConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CachedConfiguration extends CachedSection implements Configuration {

    @NotNull
    protected Configuration defaults;

    @NotNull
    protected final ConfigurationOptions options;

    public CachedConfiguration() {
        this.defaults = this;
        this.options = new ConfigurationOptions(this);
    }

    public CachedConfiguration(@NotNull Configuration defaults, @NotNull ConfigurationOptions options) {
        this.defaults = defaults;
        this.options = options;
    }

    @Override
    public void addDefault(@NotNull String path, @Nullable Object value) {
        defaults.set(path, value);
    }

    @Override
    public void addDefaults(@NotNull Map<String, Object> defaults) {
        defaults.forEach(this::addDefault);
    }

    @Override
    public void addDefaults(@NotNull Configuration defaults) {
        addDefaults(defaults.getValues(true));
    }

    @Override
    public void setDefaults(@NotNull Configuration defaults) {
        this.defaults = defaults;
    }

    @Override
    @NotNull
    public Configuration getDefaults() {
        return defaults;
    }

    @NotNull
    @Override
    public ConfigurationSection getParent() {
        return new MckConfigurationSection();
    }

    @NotNull
    @Override
    public ConfigurationOptions options() {
        return options;
    }

}
