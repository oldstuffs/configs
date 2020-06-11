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

package io.github.portlek.configs.structure.section;

import io.github.portlek.configs.annotations.ConfigSerializable;
import io.github.portlek.configs.annotations.Unstable;
import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.provided.Provided;
import io.github.portlek.configs.provided.ProvidedGet;
import io.github.portlek.configs.provided.ProvidedSet;
import io.github.portlek.configs.provided.SerializableProvider;
import io.github.portlek.configs.structure.managed.FlManaged;
import io.github.portlek.configs.util.GeneralUtilities;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CfgSection {

    Map<Class<?>, Provided<?>> PROVIDED = new ConcurrentHashMap<>();

    Map<Class<?>, ProvidedGet<?>> PROVIDED_GET = new ConcurrentHashMap<>();

    Map<Class<?>, ProvidedSet<?>> PROVIDED_SET = new ConcurrentHashMap<>();

    @NotNull
    static <T> Optional<Provided<T>> getProvidedClass(@NotNull final Class<T> aClass) {
        //noinspection unchecked
        return CfgSection.PROVIDED.keySet().stream()
            .filter(aClass::equals)
            .findFirst()
            .map(clss -> (Provided<T>) CfgSection.PROVIDED.get(clss));
    }

    static <T> void addProvidedGetMethod(@NotNull final Class<T> aClass,
                                         @NotNull final ProvidedGet<T> provide) {
        if (!CfgSection.PROVIDED_GET.containsKey(aClass)) {
            CfgSection.PROVIDED_GET.put(aClass, provide);
        }
    }

    @NotNull
    static <T> Optional<ProvidedGet<T>> getProvidedGetMethod(@NotNull final Class<T> aClass) {
        //noinspection unchecked
        return CfgSection.PROVIDED_GET.keySet().stream()
            .filter(aClass::equals)
            .findFirst()
            .map(clss -> (ProvidedGet<T>) CfgSection.PROVIDED_GET.get(clss));
    }

    static <T> void addProvidedSetMethod(@NotNull final Class<T> aClass, @NotNull final ProvidedSet<T> provide) {
        if (!CfgSection.PROVIDED_SET.containsKey(aClass)) {
            CfgSection.PROVIDED_SET.put(aClass, provide);
        }
    }

    @NotNull
    static <T> Optional<ProvidedSet<T>> getProvidedSetMethod(@NotNull final Class<T> aClass) {
        //noinspection unchecked
        return CfgSection.PROVIDED_SET.keySet().stream()
            .filter(aClass::equals)
            .findFirst()
            .map(clss -> (ProvidedSet<T>) CfgSection.PROVIDED_SET.get(clss));
    }

    @Unstable
    static <T> void addSerializableClass(@NotNull final Class<T> tclass) {
        Optional.ofNullable(tclass.getDeclaredAnnotation(ConfigSerializable.class)).orElseThrow(() ->
            new UnsupportedOperationException(tclass.getSimpleName() + " has not `ConfigSerializable` annotation!"));
        final SerializableProvider<T> provided = new SerializableProvider<>(tclass);
        provided.initiate();
        CfgSection.addProvidedClass(tclass, provided);
    }

    static <T> void addProvidedClass(@NotNull final Class<T> tclass, @NotNull final Provided<T> provided) {
        if (!CfgSection.PROVIDED.containsKey(tclass)) {
            CfgSection.PROVIDED.put(tclass, provided);
        }
    }

    @NotNull
    default String getName() {
        return this.getConfigurationSection().getName();
    }

    @NotNull
    default Set<String> getKeys(final boolean deep) {
        return this.getConfigurationSection().getKeys(deep);
    }

    default boolean contains(@NotNull final String path) {
        return this.getConfigurationSection().contains(path);
    }

    default void remove(@NotNull final String path) {
        this.set(path, null);
    }

    @NotNull
    default Optional<Object> get(@NotNull final String path, @Nullable final Object def) {
        return Optional.ofNullable(this.getConfigurationSection().get(path, def));
    }

    @NotNull
    default <T> T getOrSetGeneric(@NotNull final String path, @NotNull final T fallback,
                                  @NotNull final Function<String, Optional<T>> function) {
        return function.apply(path).orElseGet(() -> {
            this.set(path, fallback);
            return fallback;
        });
    }

    @NotNull
    default <T> Optional<T> getGeneric(@NotNull final String path,
                                       @NotNull final Function<String, T> function) {
        if (this.contains(path)) {
            return Optional.ofNullable(function.apply(path));
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

    default String getOrSetString(@NotNull final String path, @NotNull final String fallback) {
        return this.getOrSetGeneric(path, fallback, this::getString);
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

    @NotNull
    default List<Map<Object, Object>> getOrSetMapList(@NotNull final String path,
                                                      @NotNull final List<Map<Object, Object>> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getMapList);
    }

    @NotNull
    default Optional<Object> get(@NotNull final String path) {
        return Optional.ofNullable(this.getConfigurationSection().get(path));
    }

    default void set(@NotNull final String path, @Nullable final Object object) {
        this.getConfigurationSection().set(path, object);
        this.getManaged().autoSave();
    }

    @NotNull
    default CfgSection getOrCreateSection(@NotNull final String path) {
        return this.getSection(path).orElseGet(() -> this.createSection(path));
    }

    @NotNull
    default Optional<CfgSection> getSection(@NotNull final String path) {
        return Optional.ofNullable(this.getConfigurationSection().getConfigurationSection(path))
            .map(configurationsection -> {
                final CfgSection configsection = this.getNewSection().get();
                configsection.setup(this.getManaged(), configurationsection);
                return configsection;
            });
    }

    @NotNull
    default CfgSection createSection(@NotNull final String path) {
        final CfgSection configsection = this.getNewSection().get();
        configsection.setup(this.getManaged(), this.getConfigurationSection().createSection(path));
        this.getManaged().autoSave();
        return configsection;
    }

    @NotNull
    default Supplier<CfgSection> getNewSection() {
        return ConfigSection::new;
    }

    @NotNull
    default Optional<UUID> getUniqueId(@NotNull final String path) {
        return this.getString(path).flatMap(GeneralUtilities::parseUniqueId);
    }

    @NotNull
    default Optional<UUID> getUniqueId(@NotNull final String path, @Nullable final String def) {
        final Optional<UUID> uuid = this.getUniqueId(path);
        if (uuid.isPresent()) {
            return uuid;
        }
        return Optional.ofNullable(def)
            .flatMap(defUniqueId -> {
                try {
                    return Optional.of(UUID.fromString(defUniqueId));
                } catch (final IllegalArgumentException e) {
                    return Optional.empty();
                }
            });
    }

    @NotNull
    default Optional<UUID> getUniqueId(@NotNull final String path, @Nullable final UUID def) {
        final Optional<UUID> uuid = this.getUniqueId(path);
        if (uuid.isPresent()) {
            return uuid;
        }
        return Optional.ofNullable(def);
    }

    @NotNull
    default Optional<String> getString(@NotNull final String path) {
        return Optional.ofNullable(this.getConfigurationSection().getString(path));
    }

    @NotNull
    default Optional<String> getString(@NotNull final String path, @Nullable final String def) {
        return Optional.ofNullable(this.getConfigurationSection().getString(path, def));
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
        return this.getGeneric(path, this.getConfigurationSection()::getInt);
    }

    default int getInteger(@NotNull final String path, final int def) {
        return this.getConfigurationSection().getInt(path, def);
    }

    default Optional<Boolean> getBoolean(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getBoolean);
    }

    default boolean getBoolean(@NotNull final String path, final boolean def) {
        return this.getConfigurationSection().getBoolean(path, def);
    }

    default Optional<Double> getDouble(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getDouble);
    }

    default double getDouble(@NotNull final String path, final double def) {
        return this.getConfigurationSection().getDouble(path, def);
    }

    default Optional<Float> getFloat(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getFloat);
    }

    default float getFloat(@NotNull final String path, final float def) {
        return this.getConfigurationSection().getFloat(path, def);
    }

    default Optional<Long> getLong(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getLong);
    }

    default long getLong(@NotNull final String path, final long def) {
        return this.getConfigurationSection().getLong(path, def);
    }

    @NotNull
    default Optional<List<String>> getStringList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getStringList);
    }

    @NotNull
    default Optional<List<String>> getStringList(@NotNull final String path, @Nullable final List<String> def) {
        final Optional<List<String>> generic = this.getStringList(path);
        if (generic.isPresent()) {
            return generic;
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
        return this.getGeneric(path, this.getConfigurationSection()::getIntegerList);
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
        return this.getGeneric(path, this.getConfigurationSection()::getBooleanList);
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
        return this.getGeneric(path, this.getConfigurationSection()::getDoubleList);
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
        return this.getGeneric(path, this.getConfigurationSection()::getFloatList);
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
        return this.getGeneric(path, this.getConfigurationSection()::getLongList);
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
        return this.getGeneric(path, this.getConfigurationSection()::getByteList);
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
        return this.getGeneric(path, this.getConfigurationSection()::getCharacterList);
    }

    @NotNull
    default Optional<List<Character>> getCharacterList(@NotNull final String path, @Nullable final List<Character> def) {
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
        return this.getGeneric(path, this.getConfigurationSection()::getShortList);
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
    default Optional<List<Map<Object, Object>>> getMapList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getMapList);
    }

    @NotNull
    default Optional<List<Map<Object, Object>>> getMapList(@NotNull final String path,
                                                           @Nullable final List<Map<Object, Object>> def) {
        final Optional<List<Map<Object, Object>>> generic = this.getMapList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    @NotNull
    default List<Map<Object, Object>> getMapListOrEmpty(@NotNull final String path) {
        return this.getMapList(path).orElse(new ArrayList<>());
    }

    @NotNull
    default List<Map<Object, Object>> getMapListOrEmpty(@NotNull final String path,
                                                        @Nullable final List<Map<Object, Object>> def) {
        return this.getMapList(path, def).orElse(new ArrayList<>());
    }

    @NotNull
    default Optional<List<UUID>> getUniqueIdList(@NotNull final String path) {
        return this.getStringList(path).map(strings -> strings.stream()
            .map(GeneralUtilities::parseUniqueId)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList()));
    }

    @NotNull
    default Optional<List<UUID>> getUniqueIdList(@NotNull final String path,
                                                 @Nullable final List<UUID> def) {
        final Optional<List<UUID>> generic = this.getUniqueIdList(path);
        if (!generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    @NotNull
    default List<UUID> getUniqueIdListOrEmpty(@NotNull final String path) {
        return this.getUniqueIdList(path).orElse(new ArrayList<>());
    }

    @NotNull
    default List<UUID> getUniqueIdListOrEmpty(@NotNull final String path,
                                              @Nullable final List<UUID> def) {
        return this.getUniqueIdList(path, def).orElse(new ArrayList<>());
    }

    @NotNull
    default Optional<List<?>> getList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getList);
    }

    @NotNull
    default Optional<List<?>> getList(@NotNull final String path, @Nullable final List<?> def) {
        final Optional<List<?>> generic = this.getList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    @NotNull
    default List<?> getListOrEmpty(@NotNull final String path) {
        return this.getList(path).orElse(new ArrayList<>());
    }

    @NotNull
    default List<?> getListOrEmpty(@NotNull final String path, @Nullable final List<?> def) {
        return this.getList(path, def).orElse(new ArrayList<>());
    }

    void setup(@NotNull FlManaged managed, @NotNull ConfigurationSection section);

    @NotNull
    ConfigurationSection getConfigurationSection();

    @NotNull
    FlManaged getManaged();

}
