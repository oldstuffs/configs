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

package io.github.portlek.configs;

import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.processors.ConfigProceed;
import io.github.portlek.configs.util.Replaceable;
import java.io.File;
import java.io.IOException;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.FileConfiguration;

public class ManagedBase extends ConfigSectionBase implements Managed {

    private final Map<String, Object> objects = new HashMap<>();

    private final Map<Class<?>, Provided<?>> customs = new HashMap<>();

    @Nullable
    private File file;

    @SafeVarargs
    protected ManagedBase(@NotNull final Map.Entry<String, Object>... objects) {
        Arrays.asList(objects).forEach(entry ->
            this.objects.put(entry.getKey(), entry.getValue()));
        this.customs.put(Replaceable.class, new Replaceable.Provider());
    }

    @NotNull
    @Override
    public final Optional<Object> pull(@NotNull final String id) {
        return Optional.ofNullable(this.objects.get(id));
    }

    @Override
    public void setup(@NotNull final File file, @NotNull final FileConfiguration fileConfiguration) {
        this.file = file;
        this.setup(this, fileConfiguration);
    }

    @Override
    public final <T> void addCustomValue(@NotNull final Class<T> aClass, @NotNull final Provided<T> provided) {
        this.customs.put(aClass, provided);
    }

    @NotNull
    @Override
    public final <T> Optional<Provided<T>> getCustomValue(@NotNull final Class<T> aClass) {
        //noinspection unchecked
        return this.customs.keySet().stream()
            .filter(aClass::equals)
            .findFirst()
            .map(clss -> (Provided<T>) this.customs.get(clss));
    }

    @NotNull
    @Override
    public final File getFile() {
        return Objects.requireNonNull(this.file, "You have to load your class with '#load()' method");
    }

    @NotNull
    @Override
    public FileConfiguration getConfigurationSection() {
        return (FileConfiguration) super.getConfigurationSection();
    }

}
