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

package io.github.portlek.configs.files.yaml;

import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.configuration.FileConfiguration;
import io.github.portlek.configs.files.yaml.eoyaml.*;
import java.io.File;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

public final class YamlConfiguration extends FileConfiguration {

    @NotNull
    public static YamlConfiguration loadConfiguration(@NotNull final File file) {
        final YamlConfiguration config = new YamlConfiguration();
        config.load(file);
        return config;
    }

    @NotNull
    public static YamlConfiguration loadConfiguration(@NotNull final Reader reader) {
        final YamlConfiguration config = new YamlConfiguration();
        config.load(reader);
        return config;
    }

    @SneakyThrows
    @NotNull
    @Override
    public String saveToString() {
        return Yaml.createYamlDump(this.getValues(false)).dumpMapping().toString();
    }

    @SneakyThrows
    @Override
    public void loadFromString(@NotNull final String contents) {
        Optional.ofNullable(Yaml.createYamlInput(contents).readYamlMapping()).ifPresent(t ->
            this.convertMapsToSections(t, this));
    }

    @NotNull
    @Override
    public YamlConfigurationOptions options() {
        if (this.options == null) {
            this.options = new YamlConfigurationOptions(this);
        }
        return (YamlConfigurationOptions) this.options;
    }

    private void convertMapsToSections(@NotNull final YamlMapping mapping,
                                       @NotNull final ConfigurationSection section) {
        mapping.keys().stream()
            .filter(key -> key instanceof Scalar)
            .map(key -> (Scalar) key)
            .forEach(key -> {
                final YamlNode value = mapping.value(key);
                final String finalkey = key.value().replace("\"", "");
                this.convertNodeToSections(value).ifPresent(o ->
                    section.set(finalkey, o));
            });
    }

    @NotNull
    private Optional<Object> convertNodeToSections(@NotNull final YamlNode value) {
        if (value instanceof Scalar) {
            return Optional.ofNullable(((Scalar) value).getAsAll());
        }
        if (value instanceof YamlSequence) {
            return Optional.of(
                ((YamlSequence) value).values().stream()
                    .map(this::convertNodeToSections)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList()));
        }
        if (value instanceof YamlStream) {
            return Optional.of(
                ((YamlStream) value)
                    .map(this::convertNodeToSections)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList()));
        }
        if (value instanceof YamlMapping) {
            final Map<String, Object> convertedmap = new HashMap<>();
            final YamlMapping mapvalue = (YamlMapping) value;
            mapvalue.keys().forEach(node ->
                this.convertNodeToSections(mapvalue.value(node)).ifPresent(o ->
                    convertedmap.put(((Scalar) node).value(), o)));
            return Optional.of(convertedmap);
        }
        return Optional.empty();
    }

}
