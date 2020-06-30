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

package io.github.portlek.configs.files.yaml;

import com.amihaiemil.eoyaml.*;
import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.util.GeneralUtilities;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
class Helper {

    private final List<Class<?>> SCALAR_TYPES = Arrays.asList(
        Integer.class, Long.class, Float.class, Double.class, Short.class, String.class, Boolean.class, Character.class,
        Byte.class);

    public void loadFromString(@NotNull final ConfigurationSection section, @NotNull final String contents) throws IOException {
        Optional.ofNullable(Yaml.createYamlInput(contents).readYamlMapping()).ifPresent(t ->
            Helper.convertMapToSection(t, section));
    }

    @NotNull
    public YamlMapping mapAsYamlMapping(@NotNull final Map<String, Object> map) {
        final AtomicReference<YamlMappingBuilder> builder = new AtomicReference<>(Yaml.createYamlMappingBuilder());
        Helper.buildMap(builder, Helper.withoutMemorySection(map));
        return builder.get().build();
    }

    private void convertMapToSection(@NotNull final YamlMapping mapping, @NotNull final ConfigurationSection section) {
        GeneralUtilities.convertMapToSection(Helper.yamlMappingAsMap(mapping), section);
    }

    @NotNull
    private Map<String, Object> yamlMappingAsMap(@NotNull final YamlMapping mapping) {
        final Map<String, Object> map = new HashMap<>();
        mapping.keys().stream()
            .filter(node -> node instanceof Scalar)
            .map(YamlNode::asScalar)
            .forEach(keyNode ->
                Helper.yamlNodeAsObject(mapping.value(keyNode)).ifPresent(o ->
                    map.put(keyNode.value().replace("\"", ""), o)));
        return map;
    }

    @NotNull
    private Map<String, Object> withoutMemorySection(@NotNull final Map<String, Object> values) {
        final Map<String, Object> map = new HashMap<>();
        values.forEach((s, o) -> {
            if (o instanceof ConfigurationSection) {
                map.put(s, Helper.withoutMemorySection(((ConfigurationSection) o).getValues(false)));
            } else {
                map.put(s, o);
            }
        });
        return map;
    }

    private void buildMap(@NotNull final AtomicReference<YamlMappingBuilder> builder,
                          @NotNull final Map<String, Object> map) {
        map.forEach((s, o) ->
            Helper.objectAsYamlNode(o).ifPresent(node ->
                builder.set(builder.get().add(s, node))));
    }

    @NotNull
    private List<Object> sequenceAsList(@NotNull final YamlSequence sequence) {
        return sequence.values().stream()
            .map(Helper::yamlNodeAsObject)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    @NotNull
    private List<Object> streamAsList(@NotNull final YamlStream stream) {
        return stream
            .map(Helper::yamlNodeAsObject)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    private void buildSequence(@NotNull final AtomicReference<YamlSequenceBuilder> builder,
                               @NotNull final Collection<?> objects) {
        objects.stream()
            .map(Helper::objectAsYamlNode)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(node -> builder.set(builder.get().add(node)));
    }

    @NotNull
    private Optional<YamlNode> objectAsYamlNode(final Object o) {
        if (o instanceof Collection<?>) {
            final AtomicReference<YamlSequenceBuilder> sequenceBuilder = new AtomicReference<>(Yaml.createYamlSequenceBuilder());
            Helper.buildSequence(sequenceBuilder, (Collection<?>) o);
            return Optional.ofNullable(sequenceBuilder.get().build());
        }
        if (o instanceof Map<?, ?>) {
            final Map<String, Object> objectmap = (Map<String, Object>) o;
            final AtomicReference<YamlMappingBuilder> mappingBuilder = new AtomicReference<>(Yaml.createYamlMappingBuilder());
            Helper.buildMap(mappingBuilder, objectmap);
            return Optional.ofNullable(mappingBuilder.get().build());
        }
        if (Helper.SCALAR_TYPES.contains(o.getClass())) {
            final String value = String.valueOf(o);
            final AtomicReference<YamlScalarBuilder> atomic = new AtomicReference<>(Yaml.createYamlScalarBuilder());
            if (value.contains("\n")) {
                Arrays.stream(value.split("\n")).forEach(s ->
                    atomic.set(atomic.get().addLine(s)));
                return Optional.of(atomic.get().buildFoldedBlockScalar());
            }
            return Optional.ofNullable(atomic.get().addLine(value).buildPlainScalar());
        }
        return Optional.empty();
    }

    @NotNull
    private Optional<Object> yamlNodeAsObject(@NotNull final YamlNode value) {
        @Nullable final Object object;
        if (value instanceof Scalar) {
            object = Helper.getAsAll((Scalar) value);
        } else if (value instanceof YamlSequence) {
            object = Helper.sequenceAsList(value.asSequence());
        } else if (value instanceof YamlStream) {
            object = Helper.streamAsList(value.asStream());
        } else if (value instanceof YamlMapping) {
            object = Helper.yamlMappingAsMap(value.asMapping());
        } else {
            object = null;
        }
        return Optional.ofNullable(object);
    }

    @Nullable
    private Object getAsAll(@NotNull final Scalar scalar) {
        final Optional<Integer> optional1 = Helper.getAsInteger(scalar);
        if (optional1.isPresent()) {
            return optional1.get();
        }
        final Optional<Long> optional2 = Helper.getAsLong(scalar);
        if (optional2.isPresent()) {
            return optional2.get();
        }
        final Optional<Float> optional3 = Helper.getAsFloat(scalar);
        if (optional3.isPresent()) {
            return optional3.get();
        }
        final Optional<Double> optional4 = Helper.getAsDouble(scalar);
        if (optional4.isPresent()) {
            return optional4.get();
        }
        final Optional<Short> optional5 = Helper.getAsShort(scalar);
        if (optional5.isPresent()) {
            return optional5.get();
        }
        final Optional<Boolean> optional6 = Helper.getAsBoolean(scalar);
        if (optional6.isPresent()) {
            return optional6.get();
        }
        final Optional<Character> optional7 = Helper.getAsCharacter(scalar);
        if (optional7.isPresent()) {
            return optional7.get();
        }
        final Optional<Byte> optional8 = Helper.getAsByte(scalar);
        if (optional8.isPresent()) {
            return optional8.get();
        }
        return Helper.getAsString(scalar).orElse(null);
    }

    @NotNull
    private Optional<Integer> getAsInteger(@NotNull final Scalar scalar) {
        return Optional.ofNullable(scalar.value())
            .flatMap(GeneralUtilities::toInt);
    }

    @NotNull
    private Optional<Long> getAsLong(@NotNull final Scalar scalar) {
        return Optional.ofNullable(scalar.value())
            .flatMap(GeneralUtilities::toLong);
    }

    @NotNull
    private Optional<Float> getAsFloat(@NotNull final Scalar scalar) {
        return Optional.ofNullable(scalar.value())
            .flatMap(GeneralUtilities::toFloat);
    }

    @NotNull
    private Optional<Double> getAsDouble(@NotNull final Scalar scalar) {
        return Optional.ofNullable(scalar.value())
            .flatMap(GeneralUtilities::toDouble);
    }

    @NotNull
    private Optional<Short> getAsShort(@NotNull final Scalar scalar) {
        return Optional.ofNullable(scalar.value())
            .flatMap(GeneralUtilities::toShort);
    }

    @NotNull
    private Optional<String> getAsString(@NotNull final Scalar scalar) {
        return Optional.ofNullable(scalar.value());
    }

    @NotNull
    private Optional<Boolean> getAsBoolean(@NotNull final Scalar scalar) {
        return Optional.ofNullable(scalar.value())
            .flatMap(GeneralUtilities::toBoolean);
    }

    @NotNull
    private Optional<Character> getAsCharacter(@NotNull final Scalar scalar) {
        return Optional.ofNullable(scalar.value())
            .flatMap(GeneralUtilities::toCharacter);
    }

    @NotNull
    private Optional<Byte> getAsByte(@NotNull final Scalar scalar) {
        return Optional.ofNullable(scalar.value())
            .flatMap(GeneralUtilities::toByte);
    }

}
