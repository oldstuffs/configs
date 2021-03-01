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

package io.github.portlek.configs.tree;

import java.util.Map;

/**
 * Represents a source of configurable options and settings
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/Configuration.java">Bukkit
 *   Source</a>
 */
public interface Configuration extends ConfigurationSection {

  /**
   * Sets the default value of the given path as provided.
   * <p>
   * If no source {@link Configuration} was provided as a default
   * collection, then a new {@link MemoryConfiguration} will be created to
   * hold the new default value.
   * <p>
   * If value is null, the value will be removed from the default
   * Configuration source.
   *
   * @param path Path of the value to set.
   * @param value Value to set the default to.
   *
   * @throws IllegalArgumentException Thrown if path is null.
   */
  @Override
  void addDefault(String path, Object value);

  /**
   * Sets the default values of the given paths as provided.
   * <p>
   * If no source {@link Configuration} was provided as a default
   * collection, then a new {@link MemoryConfiguration} will be created to
   * hold the new default values.
   *
   * @param defaults A map of Path/Values to add to defaults.
   *
   * @throws IllegalArgumentException Thrown if defaults is null.
   */
  void addDefaults(Map<String, Object> defaults);

  /**
   * Sets the default values of the given paths as provided.
   * <p>
   * If no source {@link Configuration} was provided as a default
   * collection, then a new {@link MemoryConfiguration} will be created to
   * hold the new default value.
   * <p>
   * This method will not hold a reference to the specified Configuration,
   * nor will it automatically update if that Configuration ever changes. If
   * you require this, you should set the default source with {@link
   * #setDefaults(Configuration)}.
   *
   * @param defaults A configuration holding a list of defaults to copy.
   *
   * @throws IllegalArgumentException Thrown if defaults is null or this.
   */
  void addDefaults(Configuration defaults);

  /**
   * Gets the source {@link Configuration} for this configuration.
   * <p>
   * If no configuration source was set, but default values were added, then
   * a {@link MemoryConfiguration} will be returned. If no source was set
   * and no defaults were set, then this method will return null.
   *
   * @return Configuration source for default values, or null if none exist.
   */
  Configuration getDefaults();

  /**
   * Sets the source of all default values for this {@link Configuration}.
   * <p>
   * If a previous source was set, or previous default values were defined,
   * then they will not be copied to the new source.
   *
   * @param defaults New source of default values for this configuration.
   *
   * @throws IllegalArgumentException Thrown if defaults is null or this.
   */
  void setDefaults(Configuration defaults);

  /**
   * Gets the {@link ConfigurationOptions} for this {@link Configuration}.
   * <p>
   * All setters through this method are chainable.
   *
   * @return Options for this configuration
   */
  ConfigurationOptions options();
}
