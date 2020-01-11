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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class CachedSection implements ConfigurationSection {

    protected final Map<String, Object> cache = new LinkedHashMap<>();

    @NotNull
    private final Configuration root;

    @Nullable
    private final ConfigurationSection parent;

    @NotNull
    private final String path;

    @NotNull
    private final String fullPath;

    protected CachedSection() {
        if (!(this instanceof Configuration)) {
            throw new IllegalStateException("Cannot construct a root MemorySection when not a Configuration");
        }

        this.path = "";
        this.fullPath = "";
        this.parent = null;
        this.root = (Configuration) this;
    }

    protected CachedSection(@NotNull ConfigurationSection parent, @NotNull String path) {
        this.path = path;
        this.parent = parent;
        this.root = parent.getRoot();
        this.fullPath = createPath(parent, path);
    }

    @Override
    @NotNull
    public Configuration getRoot() {
        return root;
    }

    @Override
    @NotNull
    public String getName() {
        return path;
    }

    @Override
    @NotNull
    public Optional<ConfigurationSection> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    @NotNull
    public String getCurrentPath() {
        return fullPath;
    }

    @NotNull
    public static String createPath(@NotNull ConfigurationSection section, @Nullable String key) {
        return createPath(section, key, section.getRoot());
    }

    @NotNull
    public static String createPath(@NotNull ConfigurationSection section, @Nullable String key,
                                    @Nullable ConfigurationSection relativeTo) {
        final char separator = section.getRoot().options().pathSeparator();
        final StringBuilder builder = new StringBuilder();

        for (
            ConfigurationSection parent = section;
            parent.getParent().isPresent() && parent != relativeTo;
            parent = parent.getParent().get()
        ) {
            if (builder.length() > 0) {
                builder.insert(0, separator);
            }

            builder.insert(0, parent.getName());
        }

        if ((key != null) && (key.length() > 0)) {
            if (builder.length() > 0) {
                builder.append(separator);
            }

            builder.append(key);
        }

        return builder.toString();
    }

}
