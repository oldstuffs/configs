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

import io.github.portlek.configs.Configuration;
import io.github.portlek.configs.ConfigurationSection;
import java.util.Objects;

/**
 * Various settings for controlling the input and output of a {@link Configuration}
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/ConfigurationOptions.java">Source</a>
 */
public class ConfigurationOptions {

  private final Configuration configuration;

  private boolean copyDefaults = true;

  private int indent = 2;

  private char pathSeparator = '.';

  protected ConfigurationOptions(final Configuration configuration) {
    this.configuration = configuration;
  }

  /**
   * Returns the {@link Configuration} that this object is responsible for.
   *
   * @return Parent configuration
   */
  public Configuration configuration() {
    return this.configuration;
  }

  /**
   * Checks if the {@link Configuration} should copy values from its default
   * {@link Configuration} directly.
   *
   * @return Whether or not defaults are directly copied
   */
  public boolean copyDefaults() {
    return this.copyDefaults;
  }

  /**
   * Sets if the {@link Configuration} should copy values from its default
   * {@link Configuration} directly.
   *
   * @param value Whether or not defaults are directly copied
   *
   * @return This object, for chaining
   */
  public ConfigurationOptions copyDefaults(final boolean value) {
    this.copyDefaults = value;
    return this;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.configuration, this.pathSeparator, this.copyDefaults);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ConfigurationOptions)) {
      return false;
    }
    final ConfigurationOptions that = (ConfigurationOptions) obj;
    return this.pathSeparator == that.pathSeparator &&
      this.copyDefaults == that.copyDefaults &&
      Objects.equals(this.configuration, that.configuration);
  }

  /**
   * Sets how much spaces should be used to indent each line.
   *
   * @param value New indent
   *
   * @return This object, for chaining
   */
  public ConfigurationOptions indent(final int value) {
    this.indent = value;
    return this;
  }

  /**
   * Gets how much spaces should be used to indent each line.
   *
   * @return How much to indent by
   */
  public int indent() {
    return this.indent;
  }

  /**
   * Sets the char that will be used to separate {@link ConfigurationSection}s
   *
   * @param value Path separator
   *
   * @return This object, for chaining
   */
  public ConfigurationOptions pathSeparator(final char value) {
    this.pathSeparator = value;
    return this;
  }

  /**
   * Gets the char that will be used to separate {@link ConfigurationSection}s
   *
   * @return Path separator
   */
  public char pathSeparator() {
    return this.pathSeparator;
  }
}
