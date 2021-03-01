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
import org.jetbrains.annotations.NotNull;

/**
 * This is a {@link Configuration} implementation that does not save or load
 * from any source, and stores all values in memory only.
 * This is useful for temporary Configurations for providing defaults.
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/MemoryConfiguration.java">Bukkit
 *   Source</a>
 */
public class MemoryConfiguration extends MemorySection implements Configuration {

  protected Configuration defaults;

  protected MemoryConfigurationOptions options;

  /**
   * Creates an empty memory configuration with no default values.
   */
  public MemoryConfiguration() {
  }

  /**
   * Creates an empty memory configuration using the specified {@link
   * Configuration} as a source for all default values.
   *
   * @param defaults Default value provider
   *
   * @throws IllegalArgumentException Thrown if defaults is null
   */
  public MemoryConfiguration(final Configuration defaults) {
    this.defaults = defaults;
  }

  @Override
  public void addDefault(@NotNull final String path, final Object value) {
    if (this.defaults == null) {
      this.defaults = new MemoryConfiguration();
    }
    this.defaults.set(path, value);
  }

  @Override
  public ConfigurationSection getParent() {
    return null;
  }

  @Override
  public void addDefaults(@NotNull final Map<String, Object> defaults) {
    defaults.forEach(this::addDefault);
  }

  @Override
  public void addDefaults(@NotNull final Configuration defaults) {
    this.addDefaults(defaults.getValues(true));
  }

  @Override
  public Configuration getDefaults() {
    return this.defaults;
  }

  @Override
  public void setDefaults(@NotNull final Configuration defaults) {
    this.defaults = defaults;
  }

  @Override
  public MemoryConfigurationOptions options() {
    if (this.options == null) {
      this.options = new MemoryConfigurationOptions(this);
    }
    return this.options;
  }
}
