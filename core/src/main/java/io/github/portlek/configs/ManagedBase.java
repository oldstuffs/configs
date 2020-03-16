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
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.FileConfiguration;

public abstract class ManagedBase implements Managed {

    private final Map<String, Object> objects = new HashMap<>();

    private final Map<Class<?>, Provided<?>> customs = new HashMap<>();

    @Nullable
    private FileConfiguration fileConfiguration;

    @Nullable
    private File file;

    private boolean autoSave = false;

    @SafeVarargs
    protected ManagedBase(@NotNull final Map.Entry<String, Object>... objects) {
        Arrays.asList(objects).forEach(entry ->
            this.objects.put(entry.getKey(), entry.getValue())
        );
        this.customs.put(Replaceable.class, new Replaceable.Provider());
    }

    @NotNull
    @Override
    public final Optional<Object> pull(@NotNull final String id) {
        return Optional.ofNullable(this.objects.get(id));
    }

    @Override
    public final void setAutoSave(final boolean autoSave) {
        this.autoSave = autoSave;
    }

    @Override
    public void load() {
        final Config config = this.getClass().getDeclaredAnnotation(Config.class);
        if (config != null) {
            new ConfigProceed(config).load(this);
            return;
        }
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " has not `Config` annotation!");
    }

    @Override
    public final void save() {
        try {
            this.getFileConfiguration().save(this.getFile());
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void setup(@NotNull final File file, @NotNull final FileConfiguration fileConfiguration) {
        this.file = file;
        this.fileConfiguration = fileConfiguration;
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
    public final FileConfiguration getFileConfiguration() {
        return Objects.requireNonNull(this.fileConfiguration, "You have to load your class with '#load()' method");
    }

    @NotNull
    @Override
    public final Optional<Object> get(@NotNull final String path) {
        return Optional.ofNullable(this.getFileConfiguration().get(path));
    }

    @NotNull
    @Override
    public final Optional<Object> get(@NotNull final String path, @Nullable final Object def) {
        return Optional.ofNullable(this.getFileConfiguration().get(path, def));
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public final <T> T getOrSet(@NotNull final String path, @NotNull final T fallback) {
        final Optional<T> object = (Optional<T>) this.get(path);
        if (object.isPresent()) {
            return object.get();
        }
        this.set(path, fallback);
        this.autoSave();
        return fallback;
    }

    @Override
    public final void set(@NotNull final String path, @Nullable final Object object) {
        this.getFileConfiguration().set(path, object);
        this.autoSave();
    }

    @Override
    public final void addDefault(@NotNull final String path, @Nullable final Object object) {
        this.getFileConfiguration().addDefault(path, object);
        this.autoSave();
    }

    @NotNull
    @Override
    public final Optional<ConfigurationSection> getSection(@NotNull final String path) {
        return Optional.ofNullable(this.getFileConfiguration().getConfigurationSection(path));
    }

    @NotNull
    @Override
    public final Optional<ConfigurationSection> getOrCreateSection(@NotNull final String path) {
        final Optional<ConfigurationSection> sectionOptional = this.getSection(path);
        if (sectionOptional.isPresent()) {
            return sectionOptional;
        }
        this.createSection(path);
        return this.getSection(path);
    }

    @Override
    public final void createSection(@NotNull final String path) {
        this.getFileConfiguration().createSection(path);
        this.autoSave();
    }

    @NotNull
    @Override
    public final Optional<String> getString(@NotNull final String path) {
        return Optional.ofNullable(this.getFileConfiguration().getString(path));
    }

    @NotNull
    @Override
    public final Optional<String> getString(@NotNull final String path, @Nullable final String def) {
        return Optional.ofNullable(this.getFileConfiguration().getString(path, def));
    }

    @Override
    public final int getInt(@NotNull final String path) {
        return this.getFileConfiguration().getInt(path);
    }

    @Override
    public final int getInt(@NotNull final String path, final int def) {
        return this.getFileConfiguration().getInt(path, def);
    }

    @Override
    public final boolean getBoolean(@NotNull final String path) {
        return this.getFileConfiguration().getBoolean(path);
    }

    @Override
    public final boolean getBoolean(@NotNull final String path, final boolean def) {
        return this.getFileConfiguration().getBoolean(path, def);
    }

    @Override
    public final double getDouble(@NotNull final String path) {
        return this.getFileConfiguration().getDouble(path);
    }

    @Override
    public final double getDouble(@NotNull final String path, final double def) {
        return this.getFileConfiguration().getDouble(path, def);
    }

    @Override
    public final float getFloat(@NotNull final String path) {
        return this.getFileConfiguration().getFloat(path);
    }

    @Override
    public final float getFloat(@NotNull final String path, final float def) {
        return this.getFileConfiguration().getFloat(path, def);
    }

    @Override
    public final long getLong(@NotNull final String path) {
        return this.getFileConfiguration().getLong(path);
    }

    @Override
    public final long getLong(@NotNull final String path, final long def) {
        return this.getFileConfiguration().getLong(path, def);
    }

    @NotNull
    @Override
    public final List<String> getStringList(@NotNull final String path) {
        return this.getFileConfiguration().getStringList(path);
    }

    @NotNull
    @Override
    public final List<Integer> getIntegerList(@NotNull final String path) {
        return this.getFileConfiguration().getIntegerList(path);
    }

    @NotNull
    @Override
    public final List<Boolean> getBooleanList(@NotNull final String path) {
        return this.getFileConfiguration().getBooleanList(path);
    }

    @NotNull
    @Override
    public final List<Double> getDoubleList(@NotNull final String path) {
        return this.getFileConfiguration().getDoubleList(path);
    }

    @NotNull
    @Override
    public final List<Float> getFloatList(@NotNull final String path) {
        return this.getFileConfiguration().getFloatList(path);
    }

    @NotNull
    @Override
    public final List<Long> getLongList(@NotNull final String path) {
        return this.getFileConfiguration().getLongList(path);
    }

    @NotNull
    @Override
    public final List<Byte> getByteList(@NotNull final String path) {
        return this.getFileConfiguration().getByteList(path);
    }

    @NotNull
    @Override
    public final List<Character> getCharacterList(@NotNull final String path) {
        return this.getFileConfiguration().getCharacterList(path);
    }

    @NotNull
    @Override
    public final List<Short> getShortList(@NotNull final String path) {
        return this.getFileConfiguration().getShortList(path);
    }

    @NotNull
    @Override
    public final Optional<List<?>> getList(@NotNull final String path) {
        return Optional.ofNullable(this.getFileConfiguration().getList(path));
    }

    private void autoSave() {
        if (this.autoSave) {
            this.save();
        }
    }

}
