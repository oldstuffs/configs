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

package io.github.portlek.configs.structure.managed;

import io.github.portlek.configs.files.configuration.FileConfiguration;
import io.github.portlek.configs.provided.Provided;
import io.github.portlek.configs.provided.ReplaceableProvider;
import io.github.portlek.configs.structure.managed.section.ConfigSection;
import io.github.portlek.configs.util.Replaceable;
import io.github.portlek.configs.util.SpecialFunction;
import java.io.File;
import java.util.*;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileManaged extends ConfigSection implements FlManaged {

    private final Map<String, Object> objects = new HashMap<>();

    private final Map<Class<?>, Provided<?>> provided = new HashMap<>();

    private final Map<Class<?>, SpecialFunction<?>> providedGet = new HashMap<>();

    private final Map<Class<?>, Function<?, Object>> providedSet = new HashMap<>();

    @Nullable
    private File file;

    private boolean autosave = false;

    @SafeVarargs
    public FileManaged(@NotNull final Map.Entry<String, Object>... objects) {
        Arrays.asList(objects).forEach(entry ->
            this.addObject(entry.getKey(), entry.getValue()));
        this.provided.put(Replaceable.class, new ReplaceableProvider());
        this.providedGet.put(UUID.class, (SpecialFunction<UUID>) this::getUniqueId);
        this.providedSet.put(UUID.class, Object::toString);
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
    public final <T> void addProvidedClass(@NotNull final Class<T> aClass, @NotNull final Provided<T> provided) {
        if (!this.provided.containsKey(aClass)) {
            this.provided.put(aClass, provided);
        }
    }

    @NotNull
    @Override
    public final <T> Optional<Provided<T>> getProvidedClass(@NotNull final Class<T> aClass) {
        //noinspection unchecked
        return this.provided.keySet().stream()
            .filter(aClass::equals)
            .findFirst()
            .map(clss -> (Provided<T>) this.provided.get(clss));
    }

    @Override
    public <T> void addProvidedGetMethod(@NotNull final Class<T> aClass,
                                         @NotNull final SpecialFunction<T> provide) {
        if (!this.providedGet.containsKey(aClass)) {
            this.providedGet.put(aClass, provide);
        }
    }

    @NotNull
    @Override
    public <T> Optional<SpecialFunction<T>> getProvidedGetMethod(@NotNull final Class<T> aClass) {
        //noinspection unchecked
        return this.providedGet.keySet().stream()
            .filter(aClass::equals)
            .findFirst()
            .map(clss -> (SpecialFunction<T>) this.providedGet.get(clss));
    }

    @Override
    public <T> void addProvidedSetMethod(@NotNull final Class<T> aClass, @NotNull final Function<T, Object> provide) {
        if (!this.providedSet.containsKey(aClass)) {
            this.providedSet.put(aClass, provide);
        }
    }

    @NotNull
    @Override
    public <T> Optional<Function<T, Object>> getProvidedSetMethod(@NotNull final Class<T> aClass) {
        //noinspection unchecked
        return this.providedSet.keySet().stream()
            .filter(aClass::equals)
            .findFirst()
            .map(clss -> (Function<T, Object>) this.providedSet.get(clss));
    }

    @NotNull
    @Override
    public final File getFile() {
        return Objects.requireNonNull(this.file, "You have to load your class with '#load()' method");
    }

    @Override
    public final void addObject(@NotNull final String key, @NotNull final Object object) {
        this.objects.put(key, object);
    }

    @Override
    public final boolean isAutoSave() {
        return this.autosave;
    }

    @Override
    public final void setAutoSave(final boolean autosv) {
        this.autosave = autosv;
    }

    @Override
    public final void autoSave() {
        if (this.autosave) {
            this.save();
        }
    }

    @NotNull
    @Override
    public final FileConfiguration getConfigurationSection() {
        return (FileConfiguration) super.getConfigurationSection();
    }

}
