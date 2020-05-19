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

package io.github.portlek.configs.structure.managed.section;

import io.github.portlek.configs.files.configuration.Configuration;
import io.github.portlek.configs.files.configuration.ConfigurationSection;
import io.github.portlek.configs.structure.managed.FlManaged;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CfgSection {

    /**
     * the base of the section.
     *
     * @return a {@link CfgSection} that manage the section own.
     */
    @NotNull
    CfgSection getBase();

    /**
     * Gets the name from this individual {@link ConfigurationSection}, in the
     * path.
     * <p>
     * This will always be the final part from {@link ConfigurationSection#getCurrentPath()}, unless
     * the section is orphaned.
     *
     * @return Name from this section
     */
    @NotNull
    default String getName() {
        return this.getConfigurationSection().getName();
    }

    /**
     * a configuration section where is using in the whole {@link CfgSection}
     *
     * @return a configuration section to manage the whole section.
     * @see ConfigurationSection
     */
    @NotNull
    ConfigurationSection getConfigurationSection();

    /**
     * Gets a set containing all keys in this section.
     * <p>
     * If deep is set to true, then this will contain all the keys within any
     * child {@link ConfigurationSection}s (and their children, etc). These
     * will be in a valid path notation for you to use.
     * <p>
     * If deep is set to false, then this will contain only the keys from any
     * direct children, and not their own children.
     *
     * @param deep Whether or not to get a deep list, as opposed to a shallow
     * list.
     * @return Set from keys contained within this ConfigurationSection.
     */
    @NotNull
    default Set<String> getKeys(final boolean deep) {
        return this.getConfigurationSection().getKeys(deep);
    }

    /**
     * Checks if this {@link ConfigurationSection} contains the given path.
     * <p>
     * If the value for the requested path does not exist but a default value
     * has been specified, this will return true.
     *
     * @param path Path to check for existence.
     * @return True if this section contains the requested path, either via
     * default or being set.
     * @throws IllegalArgumentException Thrown when path is null.
     */
    default boolean contains(@NotNull final String path) {
        return this.getConfigurationSection().contains(path);
    }

    /**
     * the section's main managed class.
     *
     * @return a {@link FlManaged} class that's main class of the section.
     */
    @NotNull
    FlManaged getManaged();

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist then the specified default value will
     * returned regardless from if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param def The default value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default Optional<Object> get(@NotNull final String path, @Nullable final Object def) {
        return Optional.ofNullable(this.getConfigurationSection().get(path, def));
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @param function the function to get requested object's from the section.
     * @param <T> The requested object's type.
     * @return Requested Object.
     */
    @NotNull
    default <T> T getOrSetGeneric(@NotNull final String path, @NotNull final T fallback,
                                  @NotNull final Function<String, Optional<T>> function) {
        return function.apply(path).orElseGet(() -> {
            this.set(path, fallback);
            return fallback;
        });
    }

    /**
     * @param path
     * @param function
     * @param <T>
     * @return
     */
    @NotNull
    default <T> Optional<T> getGeneric(@NotNull final String path,
                                       @NotNull final Function<String, T> function) {
        if (this.contains(path)) {
            return Optional.ofNullable(function.apply(path));
        }
        return Optional.empty();
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     * @throws IllegalArgumentException if fallback can't convert to {@link UUID}
     * @see UUID#fromString(String)
     */
    @NotNull
    default UUID getOrSetUniqueId(@NotNull final String path, @NotNull final String fallback) {
        return this.getOrSetGeneric(path, UUID.fromString(fallback), this::getUniqueId);
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default UUID getOrSetUniqueId(@NotNull final String path, @NotNull final UUID fallback) {
        return this.getOrSetGeneric(path, fallback, this::getUniqueId);
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default String getOrSetString(@NotNull final String path, @NotNull final String fallback) {
        return this.getOrSetGeneric(path, fallback, this::getString);
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default Long getOrSetLong(@NotNull final String path, @NotNull final Long fallback) {
        return this.getOrSetGeneric(path, fallback, this::getLong);
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default Integer getOrSetInteger(@NotNull final String path, @NotNull final Integer fallback) {
        return this.getOrSetGeneric(path, fallback, this::getInteger);
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default Float getOrSetFloat(@NotNull final String path, @NotNull final Float fallback) {
        return this.getOrSetGeneric(path, fallback, this::getFloat);
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default Boolean getOrSetBoolean(@NotNull final String path, @NotNull final Boolean fallback) {
        return this.getOrSetGeneric(path, fallback, this::getBoolean);
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default Double getOrSetDouble(@NotNull final String path, @NotNull final Double fallback) {
        return this.getOrSetGeneric(path, fallback, this::getDouble);
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default List<String> getOrSetStringList(@NotNull final String path, @NotNull final List<String> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getStringList);
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default List<Integer> getOrSetIntegerList(@NotNull final String path, @NotNull final List<Integer> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getIntegerList);
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default List<Boolean> getOrSetBooleanList(@NotNull final String path, @NotNull final List<Boolean> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getBooleanList);
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default List<Byte> getOrSetByteList(@NotNull final String path, @NotNull final List<Byte> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getByteList);
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default List<Character> getOrSetCharacterList(@NotNull final String path, @NotNull final List<Character> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getCharacterList);
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default List<Double> getOrSetDoubleList(@NotNull final String path, @NotNull final List<Double> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getDoubleList);
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default List<Float> getOrSetFloatList(@NotNull final String path, @NotNull final List<Float> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getFloatList);
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default List<Long> getOrSetLongList(@NotNull final String path, @NotNull final List<Long> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getLongList);
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default List<Short> getOrSetShortList(@NotNull final String path, @NotNull final List<Short> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getShortList);
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found. And also set sets the fallback value into the path.
     * <p>
     * If the Object does not exist then the specified fallback value will
     * returned regardless from if a fallback has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the Object to get.
     * @param fallback The fallback value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    default List<Map<?, ?>> getOrSetMapList(@NotNull final String path, @NotNull final List<Map<?, ?>> fallback) {
        return this.getOrSetGeneric(path, fallback, this::getMapList);
    }

    /**
     * Gets the requested Object by path.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return {@link Optional#empty()}.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     */
    @NotNull
    default Optional<Object> get(@NotNull final String path) {
        return Optional.ofNullable(this.getConfigurationSection().get(path));
    }

    /**
     * Sets the specified path to the given value.
     * And saves the file if {@link #getManaged()}'s auto save value is true
     *
     * @param path Path from the object to set.
     * @param object New object to set the path to.
     * <p>
     * If value is null, the entry will be removed. Any existing entry will be
     * replaced, regardless from what the new value is.
     * <p>
     * Some implementations may have limitations on what you may store. See
     * their individual javadocs for details. No implementations should allow
     * you to store {@link Configuration}s or {@link ConfigurationSection}s,
     * please use {@link #createSection(String)} for that.
     * @see FlManaged#autoSave()
     */
    default void set(@NotNull final String path, @Nullable final Object object) {
        this.getConfigurationSection().set(path, object);
        this.getManaged().autoSave();
    }

    /**
     * Gets the requested section by path. Creates the section if not
     * found.
     *
     * @param path Path from the section to get.
     * @return Requested section.
     * @see #createSection(String)
     */
    @NotNull
    default CfgSection getOrCreateSection(@NotNull final String path) {
        return this.getSection(path).orElseGet(() -> this.createSection(path));
    }

    /**
     * Gets the requested section by path.
     *
     * @param path Path from the section to get.
     * @return Requested section.
     */
    @NotNull
    default Optional<CfgSection> getSection(@NotNull final String path) {
        return Optional.ofNullable(this.getConfigurationSection().getConfigurationSection(path))
            .map(configurationsection -> {
                final CfgSection configsection = this.getNewSection().get();
                configsection.setup(this.getManaged(), configurationsection);
                return configsection;
            });
    }

    /**
     * Creates the requested section by path.
     *
     * @param path Path from the section to get.
     * @return Requested section.
     */
    @NotNull
    default CfgSection createSection(@NotNull final String path) {
        final CfgSection configsection = this.getNewSection().get();
        configsection.setup(this.getManaged(), this.getConfigurationSection().createSection(path));
        this.getManaged().autoSave();
        return configsection;
    }

    /**
     * Gives a {@link Supplier<CfgSection>} to create a new section.
     *
     * @return a {@link CfgSection} supplier to create a new section.
     */
    @NotNull
    default Supplier<CfgSection> getNewSection() {
        return ConfigSection::new;
    }

    /**
     * Setups the section's main managed class and {@link ConfigurationSection} to manage itself.
     *
     * @param managed the file managed to being main managed class.
     * @param configurationSection the configuration section to manage section itself.
     */
    void setup(@NotNull FlManaged managed, @NotNull ConfigurationSection configurationSection);

    @NotNull
    default Optional<UUID> getUniqueId(@NotNull final String path) {
        try {
            final Optional<String> optional = Optional.ofNullable(this.getConfigurationSection().getString(path));
            return optional.map(UUID::fromString);
        } catch (final IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @NotNull
    default Optional<UUID> getUniqueId(@NotNull final String path, @Nullable final String def) {
        try {
            final Optional<UUID> uuid = Optional.ofNullable(this.getConfigurationSection().getString(path))
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
            final Optional<UUID> uuid = Optional.ofNullable(this.getConfigurationSection().getString(path))
                .map(UUID::fromString);
            if (uuid.isPresent()) {
                return uuid;
            }
            return Optional.ofNullable(def).flatMap(defUniqueId ->
                Optional.of(def));
        } catch (final IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @NotNull
    default Optional<String> getString(@NotNull final String path) {
        return Optional.ofNullable(this.getConfigurationSection().getString(path));
    }

    @NotNull
    default Optional<String> getString(@NotNull final String path, @Nullable final String def) {
        return Optional.ofNullable(this.getConfigurationSection().getString(path, def));
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
    default Optional<List<Integer>> getIntegerList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getIntegerList);
    }

    @NotNull
    default Optional<List<Boolean>> getBooleanList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getBooleanList);
    }

    @NotNull
    default Optional<List<Double>> getDoubleList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getDoubleList);
    }

    @NotNull
    default Optional<List<Float>> getFloatList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getFloatList);
    }

    @NotNull
    default Optional<List<Long>> getLongList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getLongList);
    }

    @NotNull
    default Optional<List<Byte>> getByteList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getByteList);
    }

    @NotNull
    default Optional<List<Character>> getCharacterList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getCharacterList);
    }

    @NotNull
    default Optional<List<Short>> getShortList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getShortList);
    }

    @NotNull
    default Optional<List<Map<?, ?>>> getMapList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getMapList);
    }

    @NotNull
    default Optional<List<?>> getList(@NotNull final String path) {
        return Optional.ofNullable(this.getConfigurationSection().getList(path));
    }

}
