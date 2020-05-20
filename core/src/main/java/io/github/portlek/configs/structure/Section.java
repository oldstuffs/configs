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

package io.github.portlek.configs.structure;

import com.google.common.reflect.TypeToken;
import io.github.portlek.configs.util.ThrowableFunction;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Section {

    @NotNull
    static Object[] getPaths(@NotNull final String path) {
        return path.split("\\.");
    }

    void setup(@NotNull Managed managed, @NotNull ConfigurationNode configurationSection);

    @NotNull
    ConfigurationNode getNode();

    @NotNull
    Managed getManaged();

    @NotNull
    default ConfigurationNode getNode(@NotNull final String path) {
        return this.getNode().getNode(Section.getPaths(path));
    }

    @NotNull
    default Set<String> getChildrenList() {
        return this.getNode().getChildrenList().stream()
            .map(ConfigurationNode::getKey)
            .filter(o -> o instanceof String)
            .map(o -> (String) o)
            .collect(Collectors.toSet());
    }

    @NotNull
    default boolean contains(@NotNull final String path) {
        return this.getNode(path).isVirtual();
    }

    @NotNull
    default Optional<Object> get(@NotNull final String path) {
        return Optional.ofNullable(this.getNode(path).getValue());
    }

    @NotNull
    default Optional<Object> get(@NotNull final String path, @Nullable final Object def) {
        return Optional.ofNullable(this.getNode(path).getValue(def));
    }

    default void set(@NotNull final String path, @Nullable final Object object) {
        this.getNode(path).setValue(object);
        this.getManaged().autoSave();
    }

    @NotNull
    default <T> T getOrSetGeneric(@NotNull final String path,
                                  @NotNull final T fallback,
                                  @NotNull final Function<String, Optional<T>> getfunction) {
        return getfunction.apply(path).orElseGet(() -> {
            this.set(path, fallback);
            return fallback;
        });
    }

    @NotNull
    default <T> Optional<T> getGeneric(@NotNull final String path,
                                       @NotNull final ThrowableFunction<ConfigurationNode, T> function) {
        if (this.contains(path)) {
            try {
                return Optional.ofNullable(function.apply(this.getNode(path)));
            } catch (final Exception ignored) {
            }
        }
        return Optional.empty();
    }

    @NotNull
    default UUID getOrSetUniqueId(@NotNull final String path, @NotNull final String fallback) {
        return this.getOrSetGeneric(path, UUID.fromString(fallback), this::getUniqueId);
    }

    @NotNull
    default UUID getOrSetUniqueId(@NotNull final String path, @NotNull final UUID fallback) {
        return this.getOrSetGeneric(path, fallback, this::getUniqueId);
    }

    @NotNull
    default String getOrSetString(@NotNull final String path, @NotNull final String fallback) {
        return this.getOrSetGeneric(path, fallback, this::getString);
    }

    @NotNull
    default Optional<UUID> getUniqueId(@NotNull final String path) {
        try {
            final Optional<String> optional = Optional.ofNullable(this.getNode(path).getString());
            return optional.map(UUID::fromString);
        } catch (final IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @NotNull
    default Long getOrSetLong(@NotNull final String path, @NotNull final Long fallback) {
        return this.getOrSetGeneric(path, fallback, this::getLong);
    }

    @NotNull
    default Integer getOrSetInteger(@NotNull final String path, @NotNull final Integer fallback) {
        return this.getOrSetGeneric(path, fallback, this::getInteger);
    }

    @NotNull
    default Float getOrSetFloat(@NotNull final String path, @NotNull final Float fallback) {
        return this.getOrSetGeneric(path, fallback, this::getFloat);
    }

    @NotNull
    default Boolean getOrSetBoolean(@NotNull final String path, @NotNull final Boolean fallback) {
        return this.getOrSetGeneric(path, fallback, this::getBoolean);
    }

    @NotNull
    default Double getOrSetDouble(@NotNull final String path, @NotNull final Double fallback) {
        return this.getOrSetGeneric(path, fallback, this::getDouble);
    }

    @NotNull
    default List<String> getOrSetStringList(@NotNull final String path, @NotNull final List<String> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getStringList);
    }

    @NotNull
    default List<Integer> getOrSetIntegerList(@NotNull final String path, @NotNull final List<Integer> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getIntegerList);
    }

    @NotNull
    default List<Boolean> getOrSetBooleanList(@NotNull final String path, @NotNull final List<Boolean> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getBooleanList);
    }

    @NotNull
    default List<Byte> getOrSetByteList(@NotNull final String path, @NotNull final List<Byte> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getByteList);
    }

    @NotNull
    default List<Character> getOrSetCharacterList(@NotNull final String path, @NotNull final List<Character> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getCharacterList);
    }

    @NotNull
    default List<Double> getOrSetDoubleList(@NotNull final String path, @NotNull final List<Double> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getDoubleList);
    }

    @NotNull
    default List<Float> getOrSetFloatList(@NotNull final String path, @NotNull final List<Float> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getFloatList);
    }

    @NotNull
    default List<Long> getOrSetLongList(@NotNull final String path, @NotNull final List<Long> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getLongList);
    }

    @NotNull
    default List<Short> getOrSetShortList(@NotNull final String path, @NotNull final List<Short> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getShortList);
    }

    default void remove(@NotNull final String path) {
        this.set(path, null);
        this.getManaged().autoSave();
    }

    @NotNull
    default Section getOrCreateSection(@NotNull final String path) {
        return this.getSection(path).orElseGet(() -> this.createSection(path));
    }

    @NotNull
    default Optional<Section> getSection(@NotNull final String path) {
        return Optional.ofNullable(this.getNode(path))
            .map(configurationNode -> {
                final Section configsection = this.getNewSection().get();
                configsection.setup(this.getManaged(), configurationNode);
                return configsection;
            });
    }

    @NotNull
    default Section createSection(@NotNull final String path) {
        final Section configsection = this.getNewSection().get();
        configsection.setup(this.getManaged(), this.getNode(path));
        this.getManaged().autoSave();
        return configsection;
    }

    @NotNull
    default Supplier<Section> getNewSection() {
        return BaseSection::new;
    }

    @NotNull
    default Optional<UUID> getUniqueId(@NotNull final String path, @Nullable final String def) {
        try {
            final Optional<UUID> uuid = this.getString(path, def)
                .map(UUID::fromString);
            if (uuid.isPresent()) {
                return uuid;
            }
            return Optional.ofNullable(def).flatMap(defUniqueId ->
                Optional.of(UUID.fromString(defUniqueId)));
        } catch (final IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @NotNull
    default Optional<UUID> getUniqueId(@NotNull final String path, @Nullable final UUID def) {
        try {
            final Optional<UUID> optional = this.getString(path)
                .map(UUID::fromString);
            if (optional.isPresent()) {
                return optional;
            }
            return Optional.ofNullable(def).flatMap(defUniqueId ->
                Optional.of(def));
        } catch (final IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @NotNull
    default Optional<String> getString(@NotNull final String path) {
        return Optional.ofNullable(this.getNode(path).getString());
    }

    @NotNull
    default Optional<String> getString(@NotNull final String path, @Nullable final String def) {
        return Optional.ofNullable(this.getNode(path).getString(def));
    }

    @NotNull
    default String getStringOrEmpty(@NotNull final String path) {
        return this.getString(path).orElse("");
    }

    @NotNull
    default String getStringOrEmpty(@NotNull final String path, @Nullable final String def) {
        return this.getString(path, def).orElse("");
    }

    default Optional<Integer> getInteger(@NotNull final String path) {
        return this.getGeneric(path, ConfigurationNode::getInt);
    }

    default int getInteger(@NotNull final String path, final int def) {
        return this.getNode(path).getInt(def);
    }

    default Optional<Boolean> getBoolean(@NotNull final String path) {
        return this.getGeneric(path, ConfigurationNode::getBoolean);
    }

    default boolean getBoolean(@NotNull final String path, final boolean def) {
        return this.getNode(path).getBoolean(def);
    }

    default Optional<Double> getDouble(@NotNull final String path) {
        return this.getGeneric(path, ConfigurationNode::getDouble);
    }

    default double getDouble(@NotNull final String path, final double def) {
        return this.getNode(path).getDouble(def);
    }

    default Optional<Float> getFloat(@NotNull final String path) {
        return this.getGeneric(path, ConfigurationNode::getFloat);
    }

    default float getFloat(@NotNull final String path, final float def) {
        return this.getNode(path).getFloat(def);
    }

    default Optional<Long> getLong(@NotNull final String path) {
        return this.getGeneric(path, ConfigurationNode::getLong);
    }

    default long getLong(@NotNull final String path, final long def) {
        return this.getNode(path).getLong(def);
    }

    @NotNull
    default Optional<List<String>> getStringList(@NotNull final String path) {
        return this.getGeneric(path, node -> node.getList(TypeToken.of(String.class)));
    }

    @NotNull
    default Optional<List<String>> getStringList(@NotNull final String path, @Nullable final List<String> def) {
        final Optional<List<String>> optional = this.getStringList(path);
        if (optional.isPresent()) {
            return optional;
        }
        return Optional.ofNullable(def);
    }

    @NotNull
    default List<String> getStringListOrEmpty(@NotNull final String path) {
        return this.getStringList(path).orElse(new ArrayList<>());
    }

    @NotNull
    default List<String> getStringListOrEmpty(@NotNull final String path, @Nullable final List<String> def) {
        return this.getStringList(path, def).orElse(new ArrayList<>());
    }

    @NotNull
    default Optional<List<Integer>> getIntegerList(@NotNull final String path) {
        return this.getGeneric(path, node -> node.getList(TypeToken.of(Integer.class)));
    }

    @NotNull
    default Optional<List<Integer>> getIntegerList(@NotNull final String path, @Nullable final List<Integer> def) {
        final Optional<List<Integer>> generic = this.getIntegerList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    @NotNull
    default List<Integer> getIntegerListOrEmpty(@NotNull final String path) {
        return this.getIntegerList(path).orElse(new ArrayList<>());
    }

    @NotNull
    default List<Integer> getIntegerListOrEmpty(@NotNull final String path, @Nullable final List<Integer> def) {
        return this.getIntegerList(path, def).orElse(new ArrayList<>());
    }

    @NotNull
    default Optional<List<Boolean>> getBooleanList(@NotNull final String path) {
        return this.getGeneric(path, node -> node.getList(TypeToken.of(Boolean.class)));
    }

    @NotNull
    default Optional<List<Boolean>> getBooleanList(@NotNull final String path, @Nullable final List<Boolean> def) {
        final Optional<List<Boolean>> generic = this.getBooleanList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    @NotNull
    default List<Boolean> getBooleanListOrEmpty(@NotNull final String path) {
        return this.getBooleanList(path).orElse(new ArrayList<>());
    }

    @NotNull
    default List<Boolean> getBooleanListOrEmpty(@NotNull final String path, @Nullable final List<Boolean> def) {
        return this.getBooleanList(path, def).orElse(new ArrayList<>());
    }

    @NotNull
    default Optional<List<Double>> getDoubleList(@NotNull final String path) {
        return this.getGeneric(path, node -> node.getList(TypeToken.of(Double.class)));
    }

    @NotNull
    default Optional<List<Double>> getDoubleList(@NotNull final String path, @Nullable final List<Double> def) {
        final Optional<List<Double>> generic = this.getDoubleList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    @NotNull
    default List<Double> getDoubleListOrEmpty(@NotNull final String path) {
        return this.getDoubleList(path).orElse(new ArrayList<>());
    }

    @NotNull
    default List<Double> getDoubleListOrEmpty(@NotNull final String path, @Nullable final List<Double> def) {
        return this.getDoubleList(path, def).orElse(new ArrayList<>());
    }

    @NotNull
    default Optional<List<Float>> getFloatList(@NotNull final String path) {
        return this.getGeneric(path, node -> node.getList(TypeToken.of(Float.class)));
    }

    @NotNull
    default Optional<List<Float>> getFloatList(@NotNull final String path, @Nullable final List<Float> def) {
        final Optional<List<Float>> generic = this.getFloatList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    @NotNull
    default List<Float> getFloatListOrEmpty(@NotNull final String path) {
        return this.getFloatList(path).orElse(new ArrayList<>());
    }

    @NotNull
    default List<Float> getFloatListOrEmpty(@NotNull final String path, @Nullable final List<Float> def) {
        return this.getFloatList(path, def).orElse(new ArrayList<>());
    }

    @NotNull
    default Optional<List<Long>> getLongList(@NotNull final String path) {
        return this.getGeneric(path, node -> node.getList(TypeToken.of(Long.class)));
    }

    @NotNull
    default Optional<List<Long>> getLongList(@NotNull final String path, @Nullable final List<Long> def) {
        final Optional<List<Long>> generic = this.getLongList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    @NotNull
    default List<Long> getLongListOrEmpty(@NotNull final String path) {
        return this.getLongList(path).orElse(new ArrayList<>());
    }

    @NotNull
    default List<Long> getLongListOrEmpty(@NotNull final String path, @Nullable final List<Long> def) {
        return this.getLongList(path, def).orElse(new ArrayList<>());
    }

    @NotNull
    default Optional<List<Byte>> getByteList(@NotNull final String path) {
        return this.getGeneric(path, node -> node.getList(TypeToken.of(Byte.class)));
    }

    @NotNull
    default Optional<List<Byte>> getByteList(@NotNull final String path, @Nullable final List<Byte> def) {
        final Optional<List<Byte>> generic = this.getByteList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    @NotNull
    default List<Byte> getByteListOrEmpty(@NotNull final String path) {
        return this.getByteList(path).orElse(new ArrayList<>());
    }

    @NotNull
    default List<Byte> getByteListOrEmpty(@NotNull final String path, @Nullable final List<Byte> def) {
        return this.getByteList(path, def).orElse(new ArrayList<>());
    }

    @NotNull
    default Optional<List<Character>> getCharacterList(@NotNull final String path) {
        return this.getGeneric(path, node -> node.getList(TypeToken.of(Character.class)));
    }

    @NotNull
    default Optional<List<Character>> getCharacterList(@NotNull final String path,
                                                       @Nullable final List<Character> def) {
        final Optional<List<Character>> generic = this.getCharacterList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    @NotNull
    default List<Character> getCharacterListOrEmpty(@NotNull final String path) {
        return this.getCharacterList(path).orElse(new ArrayList<>());
    }

    @NotNull
    default List<Character> getCharacterListOrEmpty(@NotNull final String path, @Nullable final List<Character> def) {
        return this.getCharacterList(path, def).orElse(new ArrayList<>());
    }

    @NotNull
    default Optional<List<Short>> getShortList(@NotNull final String path) {
        return this.getGeneric(path, node -> node.getList(TypeToken.of(Short.class)));
    }

    @NotNull
    default Optional<List<Short>> getShortList(@NotNull final String path, @Nullable final List<Short> def) {
        final Optional<List<Short>> generic = this.getShortList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    @NotNull
    default List<Short> getShortListOrEmpty(@NotNull final String path) {
        return this.getShortList(path).orElse(new ArrayList<>());
    }

    @NotNull
    default List<Short> getShortListOrEmpty(@NotNull final String path, @Nullable final List<Short> def) {
        return this.getShortList(path, def).orElse(new ArrayList<>());
    }

    @NotNull
    default Optional<List<?>> getList(@NotNull final Class<?> clazz, @NotNull final String path) {
        return this.getGeneric(path, node -> node.getList(TypeToken.of(clazz)));
    }

    @NotNull
    default Optional<List<?>> getList(@NotNull final Class<?> clazz, @NotNull final String path, @Nullable final List<?> def) {
        final Optional<List<?>> generic = this.getList(clazz, path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    @NotNull
    default List<?> getListOrEmpty(@NotNull final Class<?> clazz, @NotNull final String path) {
        return this.getList(clazz, path).orElse(new ArrayList<>());
    }

    @NotNull
    default List<?> getListOrEmpty(@NotNull final Class<?> clazz, @NotNull final String path, @Nullable final List<?> def) {
        return this.getList(clazz, path, def).orElse(new ArrayList<>());
    }

}
