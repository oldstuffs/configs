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

package io.github.portlek.configs.configuration;

import java.util.Map;
import org.jetbrains.annotations.NotNull;

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
   *
   * @param path Path of the value to set.
   * @param value Value to set the default to.
   *
   * @throws IllegalArgumentException Thrown if path is null.
   */
  @Override
  void addDefault(@NotNull String path, Object value);

  /**
   * Sets the default values of the given paths as provided.
   *
   * @param defaults A map of Path/Values to add to defaults.
   *
   * @throws IllegalArgumentException Thrown if defaults is null.
   */
  void addDefaults(Map<String, Object> defaults);

  /**
   * Sets the default values of the given paths as provided.
   *
   * @param defaults A configuration holding a list of defaults to copy.
   *
   * @throws IllegalArgumentException Thrown if defaults is null or this.
   */
  void addDefaults(Configuration defaults);

  /**
   * Gets the source configuration for this configuration.
   *
   * @return Configuration source for default values, or null if none exist.
   */
  Configuration getDefaults();

  /**
   * Sets the source of all default values for this configuration.
   *
   * @param defaults New source of default values for this configuration.
   *
   * @throws IllegalArgumentException Thrown if defaults is null or this.
   */
  void setDefaults(Configuration defaults);

  /**
   * Gets the {@link ConfigurationOptions} for this configuration.
   *
   * @return Options for this configuration
   */
  ConfigurationOptions options();
}
