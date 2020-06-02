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

import io.github.portlek.configs.files.configuration.ConfigurationSection;
import io.github.portlek.configs.files.configuration.FileConfiguration;
import io.github.portlek.configs.files.configuration.MemorySection;
import io.github.portlek.configs.files.yaml.eoyaml.Scalar;
import io.github.portlek.configs.files.yaml.eoyaml.Yaml;
import io.github.portlek.configs.files.yaml.eoyaml.YamlMapping;
import io.github.portlek.configs.files.yaml.eoyaml.YamlNode;
import java.io.File;
import java.io.Reader;
import java.util.Optional;
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
                if (value instanceof Scalar) {
                    section.set(key.value().replace("\"", ""), ((Scalar) value).value());
                } else if (value instanceof YamlMapping) {
                    this.convertMapsToSections((YamlMapping) value, section);
                }
            });
    }

}
