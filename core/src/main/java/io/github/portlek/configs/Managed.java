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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Managed {

    @NotNull
    Optional<Object> get(@NotNull String path);

    @NotNull
    Optional<Object> get(@NotNull String path, @Nullable Object def);

    void set(@NotNull String path, @Nullable Object object);

    @NotNull
    Optional<ConfigurationSection> getSection(@NotNull String path);

    @NotNull
    Optional<ConfigurationSection> getOrCreateSection(@NotNull String path);

    void createSection(@NotNull String path);

    @NotNull
    Optional<String> getString(@NotNull String path);

    @NotNull
    Optional<String> getString(@NotNull String path, @Nullable String def);

    int getInt(@NotNull String path);

    int getInt(@NotNull String path, int def);

    boolean getBoolean(@NotNull String path);

    boolean getBoolean(@NotNull String path, boolean def);

    double getDouble(@NotNull String path);

    double getDouble(@NotNull String path, double def);

    long getLong(@NotNull String path);

    long getLong(@NotNull String path, long def);

    @NotNull
    List<String> getStringList(@NotNull String path);

    @NotNull
    List<Integer> getIntegerList(@NotNull String path);

    @NotNull
    List<Boolean> getBooleanList(@NotNull String path);

    @NotNull
    List<Double> getDoubleList(@NotNull String path);

    @NotNull
    List<Float> getFloatList(@NotNull String path);

    @NotNull
    List<Long> getLongList(@NotNull String path);

    @NotNull
    List<Byte> getByteList(@NotNull String path);

    @NotNull
    List<Character> getCharacterList(@NotNull String path);

    @NotNull
    List<Short> getShortList(@NotNull String path);

    void setAutoSave(boolean autoSave);

    void setFileConfiguration(@NotNull File file, @NotNull FileConfiguration fileConfiguration);

}
