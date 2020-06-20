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
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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

    @NotNull
    private static Map<String, Object> withoutMemorySection(@NotNull final Map<String, Object> values) {
        final Map<String, Object> map = new HashMap<>();
        values.forEach((s, o) -> {
            if (o instanceof ConfigurationSection) {
                map.put(s, YamlConfiguration.withoutMemorySection(((ConfigurationSection) o).getValues(false)));
            } else {
                map.put(s, o);
            }
        });
        return map;
    }

    private static void buildMap(@NotNull final AtomicReference<YamlMappingBuilder> builder,
                                 @NotNull final Map<String, Object> map) {
        map.forEach((s, o) ->
            YamlConfiguration.parseNode(o).ifPresent(node ->
                builder.set(builder.get().add(s, node))));
    }

    private static void buildSequence(@NotNull final AtomicReference<YamlSequenceBuilder> builder,
                                      @NotNull final Collection<?> objects) {
        objects.stream()
            .map(YamlConfiguration::parseNode)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(node -> builder.set(builder.get().add(node)));
    }

    @NotNull
    private static Optional<YamlNode> parseNode(final Object o) {
        if (o instanceof Collection<?>) {
            final AtomicReference<YamlSequenceBuilder> sequenceBuilder = new AtomicReference<>(Yaml.createYamlSequenceBuilder());
            YamlConfiguration.buildSequence(sequenceBuilder, (Collection<?>) o);
            return Optional.ofNullable(sequenceBuilder.get().build());
        }
        if (o instanceof Map<?, ?>) {
            final Map<String, Object> objectmap = (Map<String, Object>) o;
            final AtomicReference<YamlMappingBuilder> mappingBuilder = new AtomicReference<>(Yaml.createYamlMappingBuilder());
            YamlConfiguration.buildMap(mappingBuilder, objectmap);
            return Optional.ofNullable(mappingBuilder.get().build());
        }
        if (ReflectedYamlDump.SCALAR_TYPES.contains(o.getClass())) {
            final String value = String.valueOf(o);
            final AtomicReference<YamlScalarBuilder> atomic = new AtomicReference<>(Yaml.createYamlScalarBuilder());
            if (value.contains("\n")) {
                Arrays.stream(value.split("\n")).forEach(s ->
                    atomic.set(atomic.get().addLine(s)));
                return Optional.ofNullable(atomic.get().buildFoldedBlockScalar());
            }
            return Optional.ofNullable(atomic.get().addLine(value).buildPlainScalar());
        }
        return Optional.empty();
    }

    @NotNull
    private static Optional<Object> convertNodeToSections(@NotNull final YamlNode value) {
        if (value instanceof Scalar) {
            return Optional.ofNullable(((Scalar) value).getAsAll());
        }
        if (value instanceof YamlSequence) {
            return Optional.of(
                ((YamlSequence) value).values().stream()
                    .map(YamlConfiguration::convertNodeToSections)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList()));
        }
        if (value instanceof YamlStream) {
            return Optional.of(
                ((YamlStream) value)
                    .map(YamlConfiguration::convertNodeToSections)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList()));
        }
        if (value instanceof YamlMapping) {
            final Map<String, Object> convertedmap = new HashMap<>();
            final YamlMapping mapvalue = (YamlMapping) value;
            mapvalue.keys().forEach(node ->
                YamlConfiguration.convertNodeToSections(mapvalue.value(node)).ifPresent(o ->
                    convertedmap.put(((Scalar) node).value(), o)));
            return Optional.of(convertedmap);
        }
        return Optional.empty();
    }

    @SneakyThrows
    @NotNull
    @Override
    public String saveToString() {
        final AtomicReference<YamlMappingBuilder> builder = new AtomicReference<>(Yaml.createYamlMappingBuilder());
        YamlConfiguration.buildMap(builder, YamlConfiguration.withoutMemorySection(this.getValues(false)));
        return builder.get().build().toString();
    }

    @SneakyThrows
    @Override
    public void loadFromString(@NotNull final String contents) {
        Optional.ofNullable(Yaml.createYamlInput(contents).readYamlMapping()).ifPresent(mapping ->
            mapping.keys().stream()
                .filter(key -> key instanceof Scalar)
                .map(key -> (Scalar) key)
                .forEach(key -> {
                    final YamlNode value = mapping.value(key);
                    final String finalkey = key.value().replace("\"", "");
                    YamlConfiguration.convertNodeToSections(value).ifPresent(o -> {
                        this.set(finalkey, o);
                    });
                }));
        System.out.println(getKeys(false));
    }

    @NotNull
    @Override
    public YamlConfigurationOptions options() {
        if (this.options == null) {
            this.options = new YamlConfigurationOptions(this);
        }
        return (YamlConfigurationOptions) this.options;
    }

}
