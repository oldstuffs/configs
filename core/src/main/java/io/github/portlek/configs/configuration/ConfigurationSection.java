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

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ConfigurationSection {

    @NotNull Set<String> getKeys(boolean deep);

    @NotNull Map<String, Object> getValues(boolean deep);

    boolean contains(@NotNull String path);

    boolean contains(@NotNull String path, boolean ignoreDefault);

    boolean isSet(@NotNull String path);

    @Nullable String getCurrentPath();

    @NotNull String getName();

    @Nullable Configuration getRoot();

    @Nullable ConfigurationSection getParent();

    @Nullable Object get(@NotNull String path);

    @Nullable Object get(@NotNull String path, @Nullable Object def);

    void set(@NotNull String path, @Nullable Object value);

    @NotNull ConfigurationSection createSection(@NotNull String path);

    @NotNull ConfigurationSection createSection(@NotNull String path, @NotNull Map<?, ?> map);

    // Primitives

    @Nullable String getString(@NotNull String path);

    @Nullable String getString(@NotNull String path, @Nullable String def);

    boolean isString(@NotNull String path);

    int getInt(@NotNull String path);

    int getInt(@NotNull String path, int def);

    boolean isInt(@NotNull String path);

    boolean getBoolean(@NotNull String path);

    boolean getBoolean(@NotNull String path, boolean def);

    boolean isBoolean(@NotNull String path);

    float getFloat(@NotNull String path);

    float getFloat(@NotNull String path, float def);

    double getDouble(@NotNull String path);

    double getDouble(@NotNull String path, double def);

    boolean isDouble(@NotNull String path);

    long getLong(@NotNull String path);

    long getLong(@NotNull String path, long def);

    boolean isLong(@NotNull String path);

    // Java

    @Nullable List<?> getList(@NotNull String path);

    @Nullable List<?> getList(@NotNull String path, @Nullable List<?> def);

    boolean isList(@NotNull String path);

    @NotNull List<String> getStringList(@NotNull String path);

    @NotNull List<Integer> getIntegerList(@NotNull String path);

    @NotNull List<Boolean> getBooleanList(@NotNull String path);

    @NotNull List<Double> getDoubleList(@NotNull String path);

    @NotNull List<Float> getFloatList(@NotNull String path);

    @NotNull List<Long> getLongList(@NotNull String path);

    @NotNull List<Byte> getByteList(@NotNull String path);

    @NotNull List<Character> getCharacterList(@NotNull String path);

    @NotNull List<Short> getShortList(@NotNull String path);

    @NotNull List<Map<Object, Object>> getMapList(@NotNull String path);

    // Bukkit

    @Nullable <T> T getObject(@NotNull String path, @NotNull Class<T> clazz);

    @Nullable <T> T getObject(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def);

    @Nullable ConfigurationSection getConfigurationSection(@NotNull String path);

    boolean isConfigurationSection(@NotNull String path);

    @Nullable ConfigurationSection getDefaultSection();

    void addDefault(@NotNull String path, @Nullable Object value);

}
