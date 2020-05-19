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
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @param function the function to get requested object's from the section.
     * @param <T> The requested object's type.
     * @return Requested Object.
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
     * @see ConfigurationSection#getConfigurationSection(String)
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
     * @see ConfigurationSection#createSection(String)
     */
    @NotNull
    default CfgSection createSection(@NotNull final String path) {
        final CfgSection configsection = this.getNewSection().get();
        configsection.setup(this.getManaged(), this.getConfigurationSection().createSection(path));
        this.getManaged().autoSave();
        return configsection;
    }

    /**
     * Gives a {@link Supplier} to create a new section.
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

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getString(String)
     */
    @NotNull
    default Optional<UUID> getUniqueId(@NotNull final String path) {
        try {
            final Optional<String> optional = Optional.ofNullable(this.getConfigurationSection().getString(path));
            return optional.map(UUID::fromString);
        } catch (final IllegalArgumentException e) {
            return Optional.empty();
        }
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
     * @see ConfigurationSection#getString(String, String)
     */
    @NotNull
    default Optional<UUID> getUniqueId(@NotNull final String path, @Nullable final String def) {
        try {
            final Optional<UUID> uuid = Optional.ofNullable(this.getConfigurationSection().getString(path, def))
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

    /**
     * Gets the requested Object by path.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return {@link Optional#empty()}.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getString(String)
     */
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

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getString(String)
     */
    @NotNull
    default Optional<String> getString(@NotNull final String path) {
        return Optional.ofNullable(this.getConfigurationSection().getString(path));
    }

    /**
     * Gets the requested Object by path, returning a fallback value if not
     * found.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return {@link Optional#empty()}.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getString(String, String)
     */
    @NotNull
    default Optional<String> getString(@NotNull final String path, @Nullable final String def) {
        return Optional.ofNullable(this.getConfigurationSection().getString(path, def));
    }

    /**
     * Gets the requested Object by path, returning an empty string if not
     * found.
     *
     * @param path Path from the Object to get.
     * @return Requested Object.
     * @see #getString(String)
     */
    @NotNull
    default String getStringOrEmpty(@NotNull final String path) {
        return this.getString(path).orElse("");
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found. If default value is null, returns empty string.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return empty string.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getString(String, String)
     */
    @NotNull
    default String getStringOrEmpty(@NotNull final String path, @Nullable final String def) {
        return this.getString(path, def).orElse("");
    }

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getInt(String)
     */
    default Optional<Integer> getInteger(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getInt);
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getInt(String, int)
     */
    default int getInteger(@NotNull final String path, final int def) {
        return this.getConfigurationSection().getInt(path, def);
    }

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getBoolean(String)
     */
    default Optional<Boolean> getBoolean(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getBoolean);
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getBoolean(String, boolean)
     */
    default boolean getBoolean(@NotNull final String path, final boolean def) {
        return this.getConfigurationSection().getBoolean(path, def);
    }

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getDouble(String)
     */
    default Optional<Double> getDouble(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getDouble);
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getDouble(String, double)
     */
    default double getDouble(@NotNull final String path, final double def) {
        return this.getConfigurationSection().getDouble(path, def);
    }

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getFloat(String)
     */
    default Optional<Float> getFloat(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getFloat);
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getFloat(String, float)
     */
    default float getFloat(@NotNull final String path, final float def) {
        return this.getConfigurationSection().getFloat(path, def);
    }

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getLong(String)
     */
    default Optional<Long> getLong(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getLong);
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getLong(String, long)
     */
    default long getLong(@NotNull final String path, final long def) {
        return this.getConfigurationSection().getLong(path, def);
    }

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getStringList(String)
     */
    @NotNull
    default Optional<List<String>> getStringList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getStringList);
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return {@link Optional#empty()}.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getStringList(String)
     */
    @NotNull
    default Optional<List<String>> getStringList(@NotNull final String path, @Nullable final List<String> def) {
        final Optional<List<String>> generic = this.getStringList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    /**
     * Gets the requested Object by path, returning an empty list if not
     * found.
     *
     * @param path Path from the Object to get.
     * @return Requested Object.
     * @see #getStringList(String)
     * @see ArrayList
     */
    @NotNull
    default List<String> getStringListOrEmpty(@NotNull final String path) {
        return this.getStringList(path).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found. If default value is null, returns empty list.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return empty list.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getStringList(String, List)
     * @see ArrayList
     */
    @NotNull
    default List<String> getStringListOrEmpty(@NotNull final String path, @Nullable final List<String> def) {
        return this.getStringList(path, def).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getIntegerList(String)
     */
    @NotNull
    default Optional<List<Integer>> getIntegerList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getIntegerList);
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return {@link Optional#empty()}.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getIntegerList(String)
     */
    @NotNull
    default Optional<List<Integer>> getIntegerList(@NotNull final String path, @Nullable final List<Integer> def) {
        final Optional<List<Integer>> generic = this.getIntegerList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    /**
     * Gets the requested Object by path, returning an empty list if not
     * found.
     *
     * @param path Path from the Object to get.
     * @return Requested Object.
     * @see #getIntegerList(String)
     * @see ArrayList
     */
    @NotNull
    default List<Integer> getIntegerListOrEmpty(@NotNull final String path) {
        return this.getIntegerList(path).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found. If default value is null, returns empty list.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return empty list.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getIntegerList(String, List)
     * @see ArrayList
     */
    @NotNull
    default List<Integer> getIntegerListOrEmpty(@NotNull final String path, @Nullable final List<Integer> def) {
        return this.getIntegerList(path, def).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getBooleanList(String)
     */
    @NotNull
    default Optional<List<Boolean>> getBooleanList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getBooleanList);
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return {@link Optional#empty()}.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getBooleanList(String)
     */
    @NotNull
    default Optional<List<Boolean>> getBooleanList(@NotNull final String path, @Nullable final List<Boolean> def) {
        final Optional<List<Boolean>> generic = this.getBooleanList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    /**
     * Gets the requested Object by path, returning an empty list if not
     * found.
     *
     * @param path Path from the Object to get.
     * @return Requested Object.
     * @see #getBooleanList(String)
     * @see ArrayList
     */
    @NotNull
    default List<Boolean> getBooleanListOrEmpty(@NotNull final String path) {
        return this.getBooleanList(path).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found. If default value is null, returns empty list.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return empty list.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getBooleanList(String, List)
     * @see ArrayList
     */
    @NotNull
    default List<Boolean> getBooleanListOrEmpty(@NotNull final String path, @Nullable final List<Boolean> def) {
        return this.getBooleanList(path, def).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getDoubleList(String)
     */
    @NotNull
    default Optional<List<Double>> getDoubleList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getDoubleList);
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return {@link Optional#empty()}.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getDoubleList(String)
     */
    @NotNull
    default Optional<List<Double>> getDoubleList(@NotNull final String path, @Nullable final List<Double> def) {
        final Optional<List<Double>> generic = this.getDoubleList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    /**
     * Gets the requested Object by path, returning an empty list if not
     * found.
     *
     * @param path Path from the Object to get.
     * @return Requested Object.
     * @see #getDoubleList(String)
     * @see ArrayList
     */
    @NotNull
    default List<Double> getDoubleListOrEmpty(@NotNull final String path) {
        return this.getDoubleList(path).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found. If default value is null, returns empty list.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return empty list.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getDoubleList(String, List)
     * @see ArrayList
     */
    @NotNull
    default List<Double> getDoubleListOrEmpty(@NotNull final String path, @Nullable final List<Double> def) {
        return this.getDoubleList(path, def).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getFloatList(String)
     */
    @NotNull
    default Optional<List<Float>> getFloatList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getFloatList);
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return {@link Optional#empty()}.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getFloatList(String)
     */
    @NotNull
    default Optional<List<Float>> getFloatList(@NotNull final String path, @Nullable final List<Float> def) {
        final Optional<List<Float>> generic = this.getFloatList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    /**
     * Gets the requested Object by path, returning an empty list if not
     * found.
     *
     * @param path Path from the Object to get.
     * @return Requested Object.
     * @see #getFloatList(String)
     * @see ArrayList
     */
    @NotNull
    default List<Float> getFloatListOrEmpty(@NotNull final String path) {
        return this.getFloatList(path).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found. If default value is null, returns empty list.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return empty list.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getFloatList(String, List)
     * @see ArrayList
     */
    @NotNull
    default List<Float> getFloatListOrEmpty(@NotNull final String path, @Nullable final List<Float> def) {
        return this.getFloatList(path, def).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getLongList(String)
     */
    @NotNull
    default Optional<List<Long>> getLongList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getLongList);
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return {@link Optional#empty()}.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getLongList(String)
     */
    @NotNull
    default Optional<List<Long>> getLongList(@NotNull final String path, @Nullable final List<Long> def) {
        final Optional<List<Long>> generic = this.getLongList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    /**
     * Gets the requested Object by path, returning an empty list if not
     * found.
     *
     * @param path Path from the Object to get.
     * @return Requested Object.
     * @see #getLongList(String)
     * @see ArrayList
     */
    @NotNull
    default List<Long> getLongListOrEmpty(@NotNull final String path) {
        return this.getLongList(path).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found. If default value is null, returns empty list.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return empty list.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getLongList(String, List)
     * @see ArrayList
     */
    @NotNull
    default List<Long> getLongListOrEmpty(@NotNull final String path, @Nullable final List<Long> def) {
        return this.getLongList(path, def).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getByteList(String)
     */
    @NotNull
    default Optional<List<Byte>> getByteList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getByteList);
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return {@link Optional#empty()}.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getByteList(String)
     */
    @NotNull
    default Optional<List<Byte>> getByteList(@NotNull final String path, @Nullable final List<Byte> def) {
        final Optional<List<Byte>> generic = this.getByteList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    /**
     * Gets the requested Object by path, returning an empty list if not
     * found.
     *
     * @param path Path from the Object to get.
     * @return Requested Object.
     * @see #getByteList(String)
     * @see ArrayList
     */
    @NotNull
    default List<Byte> getByteListOrEmpty(@NotNull final String path) {
        return this.getByteList(path).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found. If default value is null, returns empty list.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return empty list.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getByteList(String, List)
     * @see ArrayList
     */
    @NotNull
    default List<Byte> getByteListOrEmpty(@NotNull final String path, @Nullable final List<Byte> def) {
        return this.getByteList(path, def).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getCharacterList(String)
     */
    @NotNull
    default Optional<List<Character>> getCharacterList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getCharacterList);
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return {@link Optional#empty()}.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getCharacterList(String)
     */
    @NotNull
    default Optional<List<Character>> getCharacterList(@NotNull final String path, @Nullable final List<Character> def) {
        final Optional<List<Character>> generic = this.getCharacterList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    /**
     * Gets the requested Object by path, returning an empty list if not
     * found.
     *
     * @param path Path from the Object to get.
     * @return Requested Object.
     * @see #getCharacterList(String)
     * @see ArrayList
     */
    @NotNull
    default List<Character> getCharacterListOrEmpty(@NotNull final String path) {
        return this.getCharacterList(path).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found. If default value is null, returns empty list.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return empty list.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getCharacterList(String, List)
     * @see ArrayList
     */
    @NotNull
    default List<Character> getCharacterListOrEmpty(@NotNull final String path, @Nullable final List<Character> def) {
        return this.getCharacterList(path, def).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getShortList(String)
     */
    @NotNull
    default Optional<List<Short>> getShortList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getShortList);
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return {@link Optional#empty()}.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getShortList(String)
     */
    @NotNull
    default Optional<List<Short>> getShortList(@NotNull final String path, @Nullable final List<Short> def) {
        final Optional<List<Short>> generic = this.getShortList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    /**
     * Gets the requested Object by path, returning an empty list if not
     * found.
     *
     * @param path Path from the Object to get.
     * @return Requested Object.
     * @see #getShortList(String)
     * @see ArrayList
     */
    @NotNull
    default List<Short> getShortListOrEmpty(@NotNull final String path) {
        return this.getShortList(path).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found. If default value is null, returns empty list.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return empty list.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getShortList(String, List)
     * @see ArrayList
     */
    @NotNull
    default List<Short> getShortListOrEmpty(@NotNull final String path, @Nullable final List<Short> def) {
        return this.getShortList(path, def).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getMapList(String)
     */
    @NotNull
    default Optional<List<Map<?, ?>>> getMapList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getMapList);
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return {@link Optional#empty()}.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getMapList(String)
     */
    @NotNull
    default Optional<List<Map<?, ?>>> getMapList(@NotNull final String path, @Nullable final List<Map<?, ?>> def) {
        final Optional<List<Map<?, ?>>> generic = this.getMapList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    /**
     * Gets the requested Object by path, returning an empty list if not
     * found.
     *
     * @param path Path from the Object to get.
     * @return Requested Object.
     * @see #getMapList(String)
     * @see ArrayList
     */
    @NotNull
    default List<Map<?, ?>> getMapListOrEmpty(@NotNull final String path) {
        return this.getMapList(path).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found. If default value is null, returns empty list.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return empty list.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getDoubleList(String, List)
     * @see ArrayList
     */
    @NotNull
    default List<Map<?, ?>> getMapListOrEmpty(@NotNull final String path, @Nullable final List<Map<?, ?>> def) {
        return this.getMapList(path, def).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see ConfigurationSection#getList(String)
     */
    @NotNull
    default Optional<List<?>> getList(@NotNull final String path) {
        return this.getGeneric(path, this.getConfigurationSection()::getList);
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return {@link Optional#empty()}.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getList(String)
     */
    @NotNull
    default Optional<List<?>> getList(@NotNull final String path, @Nullable final List<?> def) {
        final Optional<List<?>> generic = this.getList(path);
        if (generic.isPresent()) {
            return generic;
        }
        return Optional.ofNullable(def);
    }

    /**
     * Gets the requested Object by path, returning an empty list if not
     * found.
     *
     * @param path Path from the Object to get.
     * @return Requested Object.
     * @see #getList(String)
     * @see ArrayList
     */
    @NotNull
    default List<?> getListOrEmpty(@NotNull final String path) {
        return this.getList(path).orElse(new ArrayList<>());
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found. If default value is null, returns empty list.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return empty list.
     *
     * @param path Path from the Object to get.
     * @return Requested Object in {@link Optional#of(Object)}.
     * @see #getList(String, List)
     * @see ArrayList
     */
    @NotNull
    default List<?> getListOrEmpty(@NotNull final String path, @Nullable final List<?> def) {
        return this.getList(path, def).orElse(new ArrayList<>());
    }

}
