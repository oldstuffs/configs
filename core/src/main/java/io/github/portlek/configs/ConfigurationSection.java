/*
 * MIT License
 *
 * Copyright (c) 2021 Hasan Demirtaş
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

package io.github.portlek.configs;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a section of a {@link Configuration}
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/ConfigurationSection.java">Bukkit
 *   Source</a>
 */
public interface ConfigurationSection {

  /**
   * Sets the default value in the root at the given path as provided.
   *
   * @param path Path of the value to set.
   * @param value Value to set the default to.
   *
   * @throws IllegalArgumentException Thrown if path is null.
   */
  void addDefault(String path, Object value);

  /**
   * Checks if this {@link ConfigurationSection} contains the given path.
   *
   * @param path Path to check for existence.
   *
   * @return True if this section contains the requested path, either via
   *   default or being set.
   *
   * @throws IllegalArgumentException Thrown when path is null.
   */
  boolean contains(String path);

  /**
   * Creates an empty {@link ConfigurationSection} at the specified path.
   *
   * @param path Path to create the section at.
   *
   * @return Newly created section
   */
  ConfigurationSection createSection(String path);

  /**
   * Creates a {@link ConfigurationSection} at the specified path, with
   * specified values.
   *
   * @param path Path to create the section at.
   * @param map The values to used.
   *
   * @return Newly created section
   */
  ConfigurationSection createSection(String path, Map<?, ?> map);

  /**
   * Gets the requested Object by path.
   *
   * @param path Path of the Object to get.
   *
   * @return Requested Object.
   */
  Object get(String path);

  /**
   * Gets the requested Object by path, returning a default value if not
   * found.
   *
   * @param path Path of the Object to get.
   * @param def The default value to return if the path is not found.
   *
   * @return Requested Object.
   */
  Object get(String path, Object def);

  /**
   * Gets the requested boolean by path.
   *
   * @param path Path of the boolean to get.
   *
   * @return Requested boolean.
   */
  boolean getBoolean(String path);

  /**
   * Gets the requested boolean by path, returning a default value if not
   * found.
   *
   * @param path Path of the boolean to get.
   * @param def The default value to return if the path is not found or is
   *   not a boolean.
   *
   * @return Requested boolean.
   */
  boolean getBoolean(String path, boolean def);

  /**
   * Gets the requested List of Boolean by path.
   *
   * @param path Path of the List to get.
   *
   * @return Requested List of Boolean.
   */
  List<Boolean> getBooleanList(String path);

  /**
   * Gets the requested List of Byte by path.
   *
   * @param path Path of the List to get.
   *
   * @return Requested List of Byte.
   */
  List<Byte> getByteList(String path);

  /**
   * Gets the requested List of Character by path.
   *
   * @param path Path of the List to get.
   *
   * @return Requested List of Character.
   */
  List<Character> getCharacterList(String path);

  /**
   * Gets the requested ConfigurationSection by path.
   *
   * @param path Path of the ConfigurationSection to get.
   *
   * @return Requested ConfigurationSection.
   */
  ConfigurationSection getConfigurationSection(String path);

  /**
   * Gets the path of this {@link ConfigurationSection} from its root {@link
   * Configuration}
   *
   * @return Path of this section relative to its root
   */
  String getCurrentPath();

  /**
   * Gets the equivalent {@link ConfigurationSection} from the default
   * {@link Configuration} defined in {@link #getRoot()}.
   *
   * @return Equivalent section in root configuration
   */
  ConfigurationSection getDefaultSection();

  /**
   * Gets the requested double by path.
   *
   * @param path Path of the double to get.
   *
   * @return Requested double.
   */
  double getDouble(String path);
  // Primitives

  /**
   * Gets the requested double by path, returning a default value if not
   * found.
   *
   * @param path Path of the double to get.
   * @param def The default value to return if the path is not found or is
   *   not a double.
   *
   * @return Requested double.
   */
  double getDouble(String path, double def);

  /**
   * Gets the requested List of Double by path.
   *
   * @param path Path of the List to get.
   *
   * @return Requested List of Double.
   */
  List<Double> getDoubleList(String path);

  /**
   * Gets the requested List of Float by path.
   *
   * @param path Path of the List to get.
   *
   * @return Requested List of Float.
   */
  List<Float> getFloatList(String path);

  /**
   * Gets the requested int by path.
   *
   * @param path Path of the int to get.
   *
   * @return Requested int.
   */
  int getInt(String path);

  /**
   * Gets the requested int by path, returning a default value if not found.
   *
   * @param path Path of the int to get.
   * @param def The default value to return if the path is not found or is
   *   not an int.
   *
   * @return Requested int.
   */
  int getInt(String path, int def);

  /**
   * Gets the requested List of Integer by path.
   *
   * @param path Path of the List to get.
   *
   * @return Requested List of Integer.
   */
  List<Integer> getIntegerList(String path);

  /**
   * Gets a set containing all keys in this section.
   *
   * @param deep Whether or not to get a deep list, as opposed to a shallow
   *   list.
   *
   * @return Set of keys contained within this ConfigurationSection.
   */
  Set<String> getKeys(boolean deep);

  /**
   * Gets the requested List by path.
   *
   * @param path Path of the List to get.
   *
   * @return Requested List.
   */
  List<?> getList(String path);

  /**
   * Gets the requested List by path, returning a default value if not
   * found.
   *
   * @param path Path of the List to get.
   * @param def The default value to return if the path is not found or is
   *   not a List.
   *
   * @return Requested List.
   */
  List<?> getList(String path, List<?> def);

  /**
   * Gets the requested long by path.
   *
   * @param path Path of the long to get.
   *
   * @return Requested long.
   */
  long getLong(String path);

  /**
   * Gets the requested long by path, returning a default value if not
   * found.
   *
   * @param path Path of the long to get.
   * @param def The default value to return if the path is not found or is
   *   not a long.
   *
   * @return Requested long.
   */
  long getLong(String path, long def);

  /**
   * Gets the requested List of Long by path.
   *
   * @param path Path of the List to get.
   *
   * @return Requested List of Long.
   */
  List<Long> getLongList(String path);

  /**
   * Gets the requested List of Maps by path.
   *
   * @param path Path of the List to get.
   *
   * @return Requested List of Maps.
   */
  List<Map<?, ?>> getMapList(String path);

  /**
   * Gets a Map containing all keys and their values for this section.
   *
   * @param deep Whether or not to get a deep list, as opposed to a shallow
   *   list.
   *
   * @return Map of keys and values of this section.
   */
  Map<String, Object> getMapValues(boolean deep);

  /**
   * Gets the name of this individual {@link ConfigurationSection}, in the
   * path.
   *
   * @return Name of this section
   */
  String getName();
  // Java

  /**
   * Gets the parent {@link ConfigurationSection} that directly contains
   * this {@link ConfigurationSection}.
   *
   * @return Parent section containing this section.
   */
  ConfigurationSection getParent();

  /**
   * Gets the root {@link Configuration} that contains this {@link
   * ConfigurationSection}
   *
   * @return Root configuration containing this section.
   */
  Configuration getRoot();

  /**
   * Gets the requested List of Short by path.
   *
   * @param path Path of the List to get.
   *
   * @return Requested List of Short.
   */
  List<Short> getShortList(String path);

  /**
   * Gets the requested String by path.
   *
   * @param path Path of the String to get.
   *
   * @return Requested String.
   */
  String getString(String path);

  /**
   * Gets the requested String by path, returning a default value if not
   * found.
   *
   * @param path Path of the String to get.
   * @param def The default value to return if the path is not found or is
   *   not a String.
   *
   * @return Requested String.
   */
  String getString(String path, String def);

  /**
   * Gets the requested List of String by path.
   *
   * @param path Path of the List to get.
   *
   * @return Requested List of String.
   */
  List<String> getStringList(String path);

  /**
   * Gets a Map containing all keys and their values for this section.
   *
   * @param deep Whether or not to get a deep list, as opposed to a shallow
   *   list.
   *
   * @return Map of keys and values of this section.
   */
  Map<String, Object> getValues(boolean deep);

  /**
   * Checks if the specified path is a boolean.
   *
   * @param path Path of the boolean to check.
   *
   * @return Whether or not the specified path is a boolean.
   */
  boolean isBoolean(String path);

  /**
   * Checks if the specified path is a ConfigurationSection.
   *
   * @param path Path of the ConfigurationSection to check.
   *
   * @return Whether or not the specified path is a ConfigurationSection.
   */
  boolean isConfigurationSection(String path);

  /**
   * Checks if the specified path is a double.
   *
   * @param path Path of the double to check.
   *
   * @return Whether or not the specified path is a double.
   */
  boolean isDouble(String path);

  /**
   * Checks if the specified path is an int.
   *
   * @param path Path of the int to check.
   *
   * @return Whether or not the specified path is an int.
   */
  boolean isInt(String path);

  /**
   * Checks if the specified path is a List.
   *
   * @param path Path of the List to check.
   *
   * @return Whether or not the specified path is a List.
   */
  boolean isList(String path);

  /**
   * Checks if the specified path is a long.
   *
   * @param path Path of the long to check.
   *
   * @return Whether or not the specified path is a long.
   */
  boolean isLong(String path);

  /**
   * Checks if this {@link ConfigurationSection} has a value set for the
   * given path.
   *
   * @param path Path to check for existence.
   *
   * @return True if this section contains the requested path, regardless of
   *   having a default.
   *
   * @throws IllegalArgumentException Thrown when path is null.
   */
  boolean isSet(String path);

  /**
   * Checks if the specified path is a String.
   *
   * @param path Path of the String to check.
   *
   * @return Whether or not the specified path is a String.
   */
  boolean isString(String path);

  /**
   * Removes the specified path if it exists.
   * The entry will be removed, either a value or an entire section.
   *
   * @param path Path of the object to remove.
   */
  default void remove(final String path) {
    this.set(path, null);
  }

  /**
   * Sets the specified path to the given value.
   *
   * @param path Path of the object to set.
   * @param value New value to set the path to.
   */
  void set(String path, Object value);
}
