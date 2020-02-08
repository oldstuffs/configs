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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;
import java.util.*;

public abstract class ManagedBase implements Managed {

    private final Map<Class<?>, Provided<?>> customValues = new HashMap<>();

    @Nullable
    private FileConfiguration fileConfiguration;

    @Nullable
    private File file;

    private boolean autoSave = false;

    @NotNull
    @Override
    public Optional<Object> get(@NotNull String path) {
        return Optional.ofNullable(getFileConfiguration().get(path));
    }

    @NotNull
    @Override
    public Optional<Object> get(@NotNull String path, @Nullable Object def) {
        return Optional.ofNullable(getFileConfiguration().get(path, def));
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getOrSet(@NotNull String path, @NotNull T fallback) {
        final Optional<T> object = (Optional<T>) get(path);

        if (object.isPresent()) {
            return object.get();
        }

        set(path, fallback);
        autoSave();

        return fallback;
    }

    @Override
    public void set(@NotNull String path, @Nullable Object object) {
        getFileConfiguration().set(path, object);
        autoSave();
    }

    @NotNull
    @Override
    public Optional<ConfigurationSection> getSection(@NotNull String path) {
        return Optional.ofNullable(getFileConfiguration().getConfigurationSection(path));
    }

    @NotNull
    @Override
    public Optional<ConfigurationSection> getOrCreateSection(@NotNull String path) {
        final Optional<ConfigurationSection> sectionOptional = getSection(path);

        if (sectionOptional.isPresent()) {
            return sectionOptional;
        }

        createSection(path);

        return getSection(path);
    }

    @Override
    public void createSection(@NotNull String path) {
        getFileConfiguration().createSection(path);
        autoSave();
    }

    @NotNull
    @Override
    public Optional<String> getString(@NotNull String path) {
        return Optional.ofNullable(getFileConfiguration().getString(path));
    }

    @NotNull
    @Override
    public Optional<String> getString(@NotNull String path, @Nullable String def) {
        return Optional.ofNullable(getFileConfiguration().getString(path, def));
    }

    @Override
    public int getInt(@NotNull String path) {
        return getFileConfiguration().getInt(path);
    }

    @Override
    public int getInt(@NotNull String path, int def) {
        return getFileConfiguration().getInt(path, def);
    }

    @Override
    public boolean getBoolean(@NotNull String path) {
        return getFileConfiguration().getBoolean(path);
    }

    @Override
    public boolean getBoolean(@NotNull String path, boolean def) {
        return getFileConfiguration().getBoolean(path, def);
    }

    @Override
    public double getDouble(@NotNull String path) {
        return getFileConfiguration().getDouble(path);
    }

    @Override
    public double getDouble(@NotNull String path, double def) {
        return getFileConfiguration().getDouble(path, def);
    }

    @Override
    public long getLong(@NotNull String path) {
        return getFileConfiguration().getLong(path);
    }

    @Override
    public long getLong(@NotNull String path, long def) {
        return getFileConfiguration().getLong(path, def);
    }

    @NotNull
    @Override
    public List<String> getStringList(@NotNull String path) {
        return getFileConfiguration().getStringList(path);
    }

    @NotNull
    @Override
    public List<Integer> getIntegerList(@NotNull String path) {
        return getFileConfiguration().getIntegerList(path);
    }

    @NotNull
    @Override
    public List<Boolean> getBooleanList(@NotNull String path) {
        return getFileConfiguration().getBooleanList(path);
    }

    @NotNull
    @Override
    public List<Double> getDoubleList(@NotNull String path) {
        return getFileConfiguration().getDoubleList(path);
    }

    @NotNull
    @Override
    public List<Float> getFloatList(@NotNull String path) {
        return getFileConfiguration().getFloatList(path);
    }

    @NotNull
    @Override
    public List<Long> getLongList(@NotNull String path) {
        return getFileConfiguration().getLongList(path);
    }

    @NotNull
    @Override
    public List<Byte> getByteList(@NotNull String path) {
        return getFileConfiguration().getByteList(path);
    }

    @NotNull
    @Override
    public List<Character> getCharacterList(@NotNull String path) {
        return getFileConfiguration().getCharacterList(path);
    }

    @NotNull
    @Override
    public List<Short> getShortList(@NotNull String path) {
        return getFileConfiguration().getShortList(path);
    }

    @NotNull
    @Override
    public Optional<List<?>> getList(@NotNull String path) {
        return Optional.ofNullable(getFileConfiguration().getList(path));
    }

    @Override
    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    @Override
    public void load() {
        final Config config = getClass().getDeclaredAnnotation(Config.class);

        if (config != null) {
            try {
                new ConfigProceed(config).load(this);

                return;
            } catch (Exception ignored) {
                // ignored
            }
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public void save() {
        try {
            getFileConfiguration().save(getFile());
        } catch (Exception exception) {
            // ignored
        }
    }

    @Override
    public void setup(@NotNull File file, @NotNull FileConfiguration fileConfiguration) {
        if (this.file != null || this.fileConfiguration != null) {
            throw new IllegalStateException("You can't use #setup(File, FileConfiguration) method twice!");
        }

        this.file = file;
        this.fileConfiguration = fileConfiguration;
    }

    @Override
    public void addCustomValue(@NotNull Class<?> aClass, @NotNull Provided<?> provided) {
        customValues.put(aClass, provided);
    }

    @NotNull
    @Override
    public Map<Class<?>, Provided<?>> getCustomValues() {
        return customValues;
    }

    @NotNull
    @Override
    public File getFile() {
        return Objects.requireNonNull(file, "You have to load your class with '#load()' method");
    }

    @NotNull
    @Override
    public FileConfiguration getFileConfiguration() {
        return Objects.requireNonNull(fileConfiguration, "You have to load your class with '#load()' method");
    }

    private void autoSave() {
        if (autoSave) {
            save();
        }
    }

}
