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

package io.github.portlek.configs.structure.comparable;

import io.github.portlek.configs.configuration.FileConfiguration;
import io.github.portlek.configs.structure.managed.FileManaged;
import io.github.portlek.configs.structure.managed.FlManaged;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ComparableManaged<S extends CmprblManaged<S>> extends FileManaged implements CmprblManaged<S> {

    private final Map<String, FlManaged> comparable = new HashMap<>();

    @Nullable
    private transient FlManaged current;

    @SafeVarargs
    protected ComparableManaged(@NotNull final Map.Entry<String, Object>... objects) {
        super(objects);
    }

    @NotNull
    @Override
    public final S key(@NotNull final String key) throws RuntimeException {
        this.current = Optional.ofNullable(this.comparable.get(key)).orElseThrow(() ->
            new RuntimeException("The key " + key + " not found!"));
        return this.self();
    }

    @NotNull
    @Override
    public final Set<String> comparableKeys() {
        return this.comparable.keySet();
    }

    @NotNull
    @Override
    public final Optional<FlManaged> comparable(@NotNull final String key) {
        return Optional.ofNullable(this.comparable.get(key));
    }

    @Override
    public final void setup(@NotNull final String key, @NotNull final FlManaged managed) {
        if (!Optional.ofNullable(this.current).isPresent()) {
            this.current = managed;
        }
        this.comparable.put(key, managed);
    }

    @NotNull
    @Override
    public final FlManaged current() {
        return Optional.ofNullable(this.current).orElseThrow(() ->
            new RuntimeException(
                "The current is null, please don't use #getConfigurationSection() before run #load() method!"));
    }

    @NotNull
    @Override
    public final File getFile() {
        return this.current().getFile();
    }

    @NotNull
    @Override
    public FileConfiguration getConfigurationSection() {
        return this.current().getConfigurationSection();
    }

    @NotNull
    @Override
    public final FlManaged getManaged() {
        return this;
    }

}
