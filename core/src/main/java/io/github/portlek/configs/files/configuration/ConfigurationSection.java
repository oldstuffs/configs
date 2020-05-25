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

package io.github.portlek.configs.files.configuration;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a section from a {@link Configuration}
 */
public interface ConfigurationSection {

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
    @NotNull Set<String> getKeys(boolean deep);

    /**
     * Gets a Map containing all keys and their values for this section.
     * <p>
     * If deep is set to true, then this will contain all the keys and values
     * within any child {@link ConfigurationSection}s (and their children,
     * etc). These keys will be in a valid path notation for you to use.
     * <p>
     * If deep is set to false, then this will contain only the keys and
     * values from any direct children, and not their own children.
     *
     * @param deep Whether or not to get a deep list, as opposed to a shallow
     * list.
     * @return Map from keys and values from this section.
     */
    @NotNull Map<String, Object> getValues(boolean deep);

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
    boolean contains(@NotNull String path);

    /**
     * Checks if this {@link ConfigurationSection} contains the given path.
     * <p>
     * If the value for the requested path does not exist, the boolean parameter
     * from true has been specified, a default value for the path exists, this
     * will return true.
     * <p>
     * If a boolean parameter from false has been specified, true will only be
     * returned if there is a set value for the specified path.
     *
     * @param path Path to check for existence.
     * @param ignoreDefault Whether or not to ignore if a default value for the
     * specified path exists.
     * @return True if this section contains the requested path, or if a default
     * value exist and the boolean parameter for this method is true.
     * @throws IllegalArgumentException Thrown when path is null.
     */
    boolean contains(@NotNull String path, boolean ignoreDefault);

    /**
     * Checks if this {@link ConfigurationSection} has a value set for the
     * given path.
     * <p>
     * If the value for the requested path does not exist but a default value
     * has been specified, this will still return false.
     *
     * @param path Path to check for existence.
     * @return True if this section contains the requested path, regardless from
     * having a default.
     * @throws IllegalArgumentException Thrown when path is null.
     */
    boolean isSet(@NotNull String path);

    /**
     * Gets the path from this {@link ConfigurationSection} from its root {@link
     * Configuration}
     * <p>
     * For any {@link Configuration} themselves, this will return an empty
     * string.
     * <p>
     * If the section is no longer contained within its root for any reason,
     * such as being replaced with a different value, this may return null.
     * <p>
     * To retrieve the single name from this section, that is, the final part from
     * the path returned by this method, you may use {@link #getName()}.
     *
     * @return Path from this section relative to its root
     */
    @Nullable String getCurrentPath();

    /**
     * Gets the name from this individual {@link ConfigurationSection}, in the
     * path.
     * <p>
     * This will always be the final part from {@link #getCurrentPath()}, unless
     * the section is orphaned.
     *
     * @return Name from this section
     */
    @NotNull String getName();

    /**
     * Gets the root {@link Configuration} that contains this {@link
     * ConfigurationSection}
     * <p>
     * For any {@link Configuration} themselves, this will return its own
     * object.
     * <p>
     * If the section is no longer contained within its root for any reason,
     * such as being replaced with a different value, this may return null.
     *
     * @return Root configuration containing this section.
     */
    @Nullable Configuration getRoot();

    /**
     * Gets the parent {@link ConfigurationSection} that directly contains
     * this {@link ConfigurationSection}.
     * <p>
     * For any {@link Configuration} themselves, this will return null.
     * <p>
     * If the section is no longer contained within its parent for any reason,
     * such as being replaced with a different value, this may return null.
     *
     * @return Parent section containing this section.
     */
    @Nullable ConfigurationSection getParent();

    /**
     * Gets the requested Object by path.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return null.
     *
     * @param path Path from the Object to get.
     * @return Requested Object.
     */
    @Nullable Object get(@NotNull String path);

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
    @Nullable Object get(@NotNull String path, @Nullable Object def);

    /**
     * Sets the specified path to the given value.
     * <p>
     * If value is null, the entry will be removed. Any existing entry will be
     * replaced, regardless from what the new value is.
     * <p>
     * Some implementations may have limitations on what you may store. See
     * their individual javadocs for details. No implementations should allow
     * you to store {@link Configuration}s or {@link ConfigurationSection}s,
     * please use {@link #createSection(String)} for that.
     *
     * @param path Path from the object to set.
     * @param value New value to set the path to.
     */
    void set(@NotNull String path, @Nullable Object value);

    /**
     * Creates an empty {@link ConfigurationSection} at the specified path.
     * <p>
     * Any value that was previously set at this path will be overwritten. If
     * the previous value was itself a {@link ConfigurationSection}, it will
     * be orphaned.
     *
     * @param path Path to create the section at.
     * @return Newly created section
     */
    @NotNull ConfigurationSection createSection(@NotNull String path);

    /**
     * Creates a {@link ConfigurationSection} at the specified path, with
     * specified values.
     * <p>
     * Any value that was previously set at this path will be overwritten. If
     * the previous value was itself a {@link ConfigurationSection}, it will
     * be orphaned.
     *
     * @param path Path to create the section at.
     * @param map The values to used.
     * @return Newly created section
     */
    @NotNull ConfigurationSection createSection(@NotNull String path, @NotNull Map<?, ?> map);

    // Primitives

    /**
     * Gets the requested String by path.
     * <p>
     * If the String does not exist but a default value has been specified,
     * this will return the default value. If the String does not exist and no
     * default value was specified, this will return null.
     *
     * @param path Path from the String to get.
     * @return Requested String.
     */
    @Nullable String getString(@NotNull String path);

    /**
     * Gets the requested String by path, returning a default value if not
     * found.
     * <p>
     * If the String does not exist then the specified default value will
     * returned regardless from if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the String to get.
     * @param def The default value to return if the path is not found or is
     * not a String.
     * @return Requested String.
     */
    @Nullable String getString(@NotNull String path, @Nullable String def);

    /**
     * Checks if the specified path is a String.
     * <p>
     * If the path exists but is not a String, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a String and return appropriately.
     *
     * @param path Path from the String to check.
     * @return Whether or not the specified path is a String.
     */
    boolean isString(@NotNull String path);

    /**
     * Gets the requested int by path.
     * <p>
     * If the int does not exist but a default value has been specified, this
     * will return the default value. If the int does not exist and no default
     * value was specified, this will return 0.
     *
     * @param path Path from the int to get.
     * @return Requested int.
     */
    int getInt(@NotNull String path);

    /**
     * Gets the requested int by path, returning a default value if not found.
     * <p>
     * If the int does not exist then the specified default value will
     * returned regardless from if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the int to get.
     * @param def The default value to return if the path is not found or is
     * not an int.
     * @return Requested int.
     */
    int getInt(@NotNull String path, int def);

    /**
     * Checks if the specified path is an int.
     * <p>
     * If the path exists but is not a int, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a int and return appropriately.
     *
     * @param path Path from the int to check.
     * @return Whether or not the specified path is an int.
     */
    boolean isInt(@NotNull String path);

    /**
     * Gets the requested boolean by path.
     * <p>
     * If the boolean does not exist but a default value has been specified,
     * this will return the default value. If the boolean does not exist and
     * no default value was specified, this will return false.
     *
     * @param path Path from the boolean to get.
     * @return Requested boolean.
     */
    boolean getBoolean(@NotNull String path);

    /**
     * Gets the requested boolean by path, returning a default value if not
     * found.
     * <p>
     * If the boolean does not exist then the specified default value will
     * returned regardless from if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the boolean to get.
     * @param def The default value to return if the path is not found or is
     * not a boolean.
     * @return Requested boolean.
     */
    boolean getBoolean(@NotNull String path, boolean def);

    /**
     * Checks if the specified path is a boolean.
     * <p>
     * If the path exists but is not a boolean, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a boolean and return appropriately.
     *
     * @param path Path from the boolean to check.
     * @return Whether or not the specified path is a boolean.
     */
    boolean isBoolean(@NotNull String path);

    /**
     * Gets the requested float by path.
     * <p>
     * If the float does not exist but a default value has been specified,
     * this will return the default value. If the float does not exist and no
     * default value was specified, this will return 0.0f.
     *
     * @param path Path from the float to get.
     * @return Requested float.
     */
    float getFloat(@NotNull String path);

    /**
     * Gets the requested float by path, returning a default value if not
     * found.
     * <p>
     * If the float does not exist then the specified default value will
     * returned regardless from if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the float to get.
     * @param def The default value to return if the path is not found or is
     * not a float.
     * @return Requested float.
     */
    float getFloat(@NotNull String path, float def);

    /**
     * Gets the requested double by path.
     * <p>
     * If the double does not exist but a default value has been specified,
     * this will return the default value. If the double does not exist and no
     * default value was specified, this will return 0.
     *
     * @param path Path from the double to get.
     * @return Requested double.
     */
    double getDouble(@NotNull String path);

    /**
     * Gets the requested double by path, returning a default value if not
     * found.
     * <p>
     * If the double does not exist then the specified default value will
     * returned regardless from if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the double to get.
     * @param def The default value to return if the path is not found or is
     * not a double.
     * @return Requested double.
     */
    double getDouble(@NotNull String path, double def);

    /**
     * Checks if the specified path is a double.
     * <p>
     * If the path exists but is not a double, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a double and return appropriately.
     *
     * @param path Path from the double to check.
     * @return Whether or not the specified path is a double.
     */
    boolean isDouble(@NotNull String path);

    /**
     * Gets the requested long by path.
     * <p>
     * If the long does not exist but a default value has been specified, this
     * will return the default value. If the long does not exist and no
     * default value was specified, this will return 0.
     *
     * @param path Path from the long to get.
     * @return Requested long.
     */
    long getLong(@NotNull String path);

    /**
     * Gets the requested long by path, returning a default value if not
     * found.
     * <p>
     * If the long does not exist then the specified default value will
     * returned regardless from if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the long to get.
     * @param def The default value to return if the path is not found or is
     * not a long.
     * @return Requested long.
     */
    long getLong(@NotNull String path, long def);

    /**
     * Checks if the specified path is a long.
     * <p>
     * If the path exists but is not a long, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a long and return appropriately.
     *
     * @param path Path from the long to check.
     * @return Whether or not the specified path is a long.
     */
    boolean isLong(@NotNull String path);

    // Java

    /**
     * Gets the requested List by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return null.
     *
     * @param path Path from the List to get.
     * @return Requested List.
     */
    @Nullable List<?> getList(@NotNull String path);

    /**
     * Gets the requested List by path, returning a default value if not
     * found.
     * <p>
     * If the List does not exist then the specified default value will
     * returned regardless from if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path from the List to get.
     * @param def The default value to return if the path is not found or is
     * not a List.
     * @return Requested List.
     */
    @Nullable List<?> getList(@NotNull String path, @Nullable List<?> def);

    /**
     * Checks if the specified path is a List.
     * <p>
     * If the path exists but is not a List, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a List and return appropriately.
     *
     * @param path Path from the List to check.
     * @return Whether or not the specified path is a List.
     */
    boolean isList(@NotNull String path);

    /**
     * Gets the requested List from String by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a String if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path from the List to get.
     * @return Requested List from String.
     */
    @NotNull List<String> getStringList(@NotNull String path);

    /**
     * Gets the requested List from Integer by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Integer if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path from the List to get.
     * @return Requested List from Integer.
     */
    @NotNull List<Integer> getIntegerList(@NotNull String path);

    /**
     * Gets the requested List from Boolean by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Boolean if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path from the List to get.
     * @return Requested List from Boolean.
     */
    @NotNull List<Boolean> getBooleanList(@NotNull String path);

    /**
     * Gets the requested List from Double by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Double if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path from the List to get.
     * @return Requested List from Double.
     */
    @NotNull List<Double> getDoubleList(@NotNull String path);

    /**
     * Gets the requested List from Float by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Float if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path from the List to get.
     * @return Requested List from Float.
     */
    @NotNull List<Float> getFloatList(@NotNull String path);

    /**
     * Gets the requested List from Long by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Long if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path from the List to get.
     * @return Requested List from Long.
     */
    @NotNull List<Long> getLongList(@NotNull String path);

    /**
     * Gets the requested List from Byte by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Byte if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path from the List to get.
     * @return Requested List from Byte.
     */
    @NotNull List<Byte> getByteList(@NotNull String path);

    /**
     * Gets the requested List from Character by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Character if
     * possible, but may miss any values out if they are not compatible.
     *
     * @param path Path from the List to get.
     * @return Requested List from Character.
     */
    @NotNull List<Character> getCharacterList(@NotNull String path);

    /**
     * Gets the requested List from Short by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Short if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path from the List to get.
     * @return Requested List from Short.
     */
    @NotNull List<Short> getShortList(@NotNull String path);

    /**
     * Gets the requested List from Maps by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Map if possible, but
     * may miss any values out if they are not compatible.
     *
     * @param path Path from the List to get.
     * @return Requested List from Maps.
     */
    @NotNull List<Map<Object, Object>> getMapList(@NotNull String path);

    // Bukkit

    /**
     * Gets the requested object at the given path.
     * <p>
     * If the Object does not exist but a default value has been specified, this
     * will return the default value. If the Object does not exist and no
     * default value was specified, this will return null.
     *
     * <b>Note:</b> For example #getObject(path, String.class) is <b>not</b>
     * equivalent to {@link #getString(String) #getString(path)} because
     * {@link #getString(String) #getString(path)} converts internally all
     * Objects to Strings. However, #getObject(path, Boolean.class) is
     * equivalent to {@link #getBoolean(String) #getBoolean(path)} for example.
     *
     * @param <T> the type from the requested object
     * @param path the path to the object.
     * @param clazz the type from the requested object
     * @return Requested object
     */
    @Nullable <T> T getObject(@NotNull String path, @NotNull Class<T> clazz);

    /**
     * Gets the requested object at the given path, returning a default value if
     * not found
     * <p>
     * If the Object does not exist then the specified default value will
     * returned regardless from if a default has been identified in the root
     * {@link Configuration}.
     *
     * <b>Note:</b> For example #getObject(path, String.class, def) is
     * <b>not</b> equivalent to
     * {@link #getString(String, String) #getString(path, def)} because
     * {@link #getString(String, String) #getString(path, def)} converts
     * internally all Objects to Strings. However, #getObject(path,
     * Boolean.class, def) is equivalent to {@link #getBoolean(String, boolean) #getBoolean(path,
     * def)} for example.
     *
     * @param <T> the type from the requested object
     * @param path the path to the object.
     * @param clazz the type from the requested object
     * @param def the default object to return if the object is not present at
     * the path
     * @return Requested object
     */
    @Nullable <T> T getObject(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def);

    /**
     * Gets the requested ConfigurationSection by path.
     * <p>
     * If the ConfigurationSection does not exist but a default value has been
     * specified, this will return the default value. If the
     * ConfigurationSection does not exist and no default value was specified,
     * this will return null.
     *
     * @param path Path from the ConfigurationSection to get.
     * @return Requested ConfigurationSection.
     */
    @Nullable ConfigurationSection getConfigurationSection(@NotNull String path);

    /**
     * Checks if the specified path is a ConfigurationSection.
     * <p>
     * If the path exists but is not a ConfigurationSection, this will return
     * false. If the path does not exist, this will return false. If the path
     * does not exist but a default value has been specified, this will check
     * if that default value is a ConfigurationSection and return
     * appropriately.
     *
     * @param path Path from the ConfigurationSection to check.
     * @return Whether or not the specified path is a ConfigurationSection.
     */
    boolean isConfigurationSection(@NotNull String path);

    /**
     * Gets the equivalent {@link ConfigurationSection} from the default
     * {@link Configuration} defined in {@link #getRoot()}.
     * <p>
     * If the root contains no defaults, or the defaults doesn't contain a
     * value for this path, or the value at this path is not a {@link
     * ConfigurationSection} then this will return null.
     *
     * @return Equivalent section in root configuration
     */
    @Nullable ConfigurationSection getDefaultSection();

    /**
     * Sets the default value in the root at the given path as provided.
     * <p>
     * If no source {@link Configuration} was provided as a default
     * collection, then a new {@link MemoryConfiguration} will be created to
     * hold the new default value.
     * <p>
     * If value is null, the value will be removed from the default
     * Configuration source.
     * <p>
     * If the value as returned by {@link #getDefaultSection()} is null, then
     * this will create a new section at the path, replacing anything that may
     * have existed there previously.
     *
     * @param path Path from the value to set.
     * @param value Value to set the default to.
     * @throws IllegalArgumentException Thrown if path is null.
     */
    void addDefault(@NotNull String path, @Nullable Object value);

}
