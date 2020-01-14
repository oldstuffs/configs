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

import io.github.portlek.configs.util.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

public abstract class ManagedBase implements Managed {

    @Nullable
    private FileConfiguration fileConfiguration;

    @Nullable
    private File file;

    private boolean autoSave = false;

    @NotNull
    @Override
    public Optional<Object> get(@NotNull String path) {
        return get(path, null);
    }

    @NotNull
    @Override
    public Optional<Object> get(@NotNull String path, @Nullable Object def) {
        validate();

        return Optional.ofNullable(Objects.requireNonNull(fileConfiguration).get(path, def));
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

        return fallback;
    }

    @Override
    public void set(@NotNull String path, @Nullable Object object) {
        validate();
        Objects.requireNonNull(fileConfiguration).set(path, object);
        autoSave();
    }

    @NotNull
    @Override
    public Optional<ConfigurationSection> getSection(@NotNull String path) {
        validate();

        return Optional.ofNullable(Objects.requireNonNull(fileConfiguration).getConfigurationSection(path));
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
        validate();
        Objects.requireNonNull(fileConfiguration).createSection(path);
        autoSave();
    }

    @NotNull
    @Override
    public Optional<String> getString(@NotNull String path) {
        return getString(path, null);
    }

    @NotNull
    @Override
    public Optional<String> getString(@NotNull String path, @Nullable String def) {
        validate();
        return Optional.ofNullable(Objects.requireNonNull(fileConfiguration).getString(path, def));
    }

    @Override
    public int getInt(@NotNull String path) {
        return getInt(path, 0);
    }

    @Override
    public int getInt(@NotNull String path, int def) {
        validate();
        return Objects.requireNonNull(fileConfiguration).getInt(path, def);
    }

    @Override
    public boolean getBoolean(@NotNull String path) {
        return getBoolean(path, false);
    }

    @Override
    public boolean getBoolean(@NotNull String path, boolean def) {
        validate();
        return Objects.requireNonNull(fileConfiguration).getBoolean(path, def);
    }

    @Override
    public double getDouble(@NotNull String path) {
        return getDouble(path, 0);
    }

    @Override
    public double getDouble(@NotNull String path, double def) {
        validate();
        return Objects.requireNonNull(fileConfiguration).getDouble(path, def);
    }

    @Override
    public long getLong(@NotNull String path) {
        return getLong(path, 0);
    }

    @Override
    public long getLong(@NotNull String path, long def) {
        validate();
        return Objects.requireNonNull(fileConfiguration).getLong(path, def);
    }

    @NotNull
    @Override
    public List<String> getStringList(@NotNull String path) {
        validate();
        return Objects.requireNonNull(fileConfiguration).getStringList(path);
    }

    @NotNull
    @Override
    public List<Integer> getIntegerList(@NotNull String path) {
        validate();
        return Objects.requireNonNull(fileConfiguration).getIntegerList(path);
    }

    @NotNull
    @Override
    public List<Boolean> getBooleanList(@NotNull String path) {
        validate();
        return Objects.requireNonNull(fileConfiguration).getBooleanList(path);
    }

    @NotNull
    @Override
    public List<Double> getDoubleList(@NotNull String path) {
        validate();
        return Objects.requireNonNull(fileConfiguration).getDoubleList(path);
    }

    @NotNull
    @Override
    public List<Float> getFloatList(@NotNull String path) {
        validate();
        return Objects.requireNonNull(fileConfiguration).getFloatList(path);
    }

    @NotNull
    @Override
    public List<Long> getLongList(@NotNull String path) {
        validate();
        return Objects.requireNonNull(fileConfiguration).getLongList(path);
    }

    @NotNull
    @Override
    public List<Byte> getByteList(@NotNull String path) {
        validate();
        return Objects.requireNonNull(fileConfiguration).getByteList(path);
    }

    @NotNull
    @Override
    public List<Character> getCharacterList(@NotNull String path) {
        validate();
        return Objects.requireNonNull(fileConfiguration).getCharacterList(path);
    }

    @NotNull
    @Override
    public List<Short> getShortList(@NotNull String path) {
        validate();
        return Objects.requireNonNull(fileConfiguration).getShortList(path);
    }

    @Override
    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    @Override
    public void setFileConfiguration(@NotNull File file, @NotNull FileConfiguration fileConfiguration) {
        if (this.file != null || this.fileConfiguration != null) {
            throw new IllegalStateException("You can't use #setFileConfigutaion after it's set!");
        }

        this.file = file;
        this.fileConfiguration = fileConfiguration;
    }

    private void autoSave() {
        if (autoSave) {
            validate();

            try {
                Objects.requireNonNull(fileConfiguration).save(Objects.requireNonNull(file));
            } catch (Exception exception) {
                Logger.getGlobal().severe(exception.getMessage());
            }
        }
    }

    private void validate() {
        Validate.isNull(fileConfiguration, "You must load your class with the 'io.github.portlek.configs.Proceed'");
        Validate.isNull(file, "You must load your class with the 'io.github.portlek.configs.Proceed'");
    }

}
