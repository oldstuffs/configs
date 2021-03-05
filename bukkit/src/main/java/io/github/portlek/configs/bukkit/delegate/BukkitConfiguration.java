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

package io.github.portlek.configs.bukkit.delegate;

import io.github.portlek.configs.configuration.Configuration;
import java.util.Map;
import lombok.experimental.Delegate;
import org.bukkit.configuration.ConfigurationOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BukkitConfiguration extends BukkitConfigurationSection implements org.bukkit.configuration.Configuration {

  @NotNull
  @Delegate(excludes = Exclusions.class)
  private final Configuration configuration;

  public BukkitConfiguration(@NotNull final Configuration configuration) {
    super(configuration);
    this.configuration = configuration;
  }

  @Override
  public void addDefaults(@NotNull final org.bukkit.configuration.Configuration defaults) {
    new BukkitConfiguration(this.configuration).addDefaults(defaults);
  }

  private interface Exclusions {

    org.bukkit.configuration.ConfigurationSection createSection(String path);

    org.bukkit.configuration.ConfigurationSection createSection(String path, Map<?, ?> map);

    org.bukkit.configuration.ConfigurationSection getConfigurationSection(String path);

    org.bukkit.configuration.ConfigurationSection getDefaultSection();

    @Nullable org.bukkit.configuration.Configuration getDefaults();

    org.bukkit.configuration.ConfigurationSection getParent();

    org.bukkit.configuration.Configuration getRoot();

    @NotNull ConfigurationOptions options();
  }

  @Override
  public void setDefaults(@NotNull final org.bukkit.configuration.Configuration defaults) {
    new BukkitConfiguration(this.configuration).setDefaults(defaults);
  }

  @Nullable
  @Override
  public org.bukkit.configuration.Configuration getDefaults() {
    final var configuration = this.configuration.getDefaults();
    return configuration == null ? null : new BukkitConfiguration(configuration);
  }

  @NotNull
  @Override
  public ConfigurationOptions options() {
    return new BukkitConfigurationOptions(this);
  }
}
