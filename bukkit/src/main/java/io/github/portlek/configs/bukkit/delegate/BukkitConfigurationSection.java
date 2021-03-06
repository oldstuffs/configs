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

import io.github.portlek.configs.configuration.ConfigurationSection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that delegates {@link ConfigurationSection} for {@link org.bukkit.configuration.ConfigurationSection}.
 */
@RequiredArgsConstructor
public class BukkitConfigurationSection implements org.bukkit.configuration.ConfigurationSection {

  /**
   * The section to delegate.
   */
  @NotNull
  @Delegate(excludes = Exclusions.class)
  private final ConfigurationSection section;

  @Nullable
  @Override
  public Configuration getRoot() {
    final var configuration = this.section.getRoot();
    return configuration == null ? null : new BukkitConfiguration(configuration);
  }

  @Nullable
  @Override
  public org.bukkit.configuration.ConfigurationSection getParent() {
    final var section = this.section.getParent();
    return section == null ? null : new BukkitConfigurationSection(section);
  }

  @NotNull
  @Override
  public org.bukkit.configuration.ConfigurationSection createSection(@NotNull final String path) {
    return new BukkitConfigurationSection(this.section.createSection(path));
  }

  @NotNull
  @Override
  public org.bukkit.configuration.ConfigurationSection createSection(@NotNull final String path,
                                                                     @NotNull final Map<?, ?> map) {
    return new BukkitConfigurationSection(this.section.createSection(path, map));
  }

  @Nullable
  @Override
  public <T extends ConfigurationSerializable> T getSerializable(@NotNull final String path,
                                                                 @NotNull final Class<T> clazz) {
    return null;
  }

  @Nullable
  @Override
  public <T extends ConfigurationSerializable> T getSerializable(@NotNull final String path,
                                                                 @NotNull final Class<T> clazz, @Nullable final T def) {
    return null;
  }

  @Nullable
  @Override
  public Vector getVector(@NotNull final String path) {
    return null;
  }

  @Nullable
  @Override
  public Vector getVector(@NotNull final String path, @Nullable final Vector def) {
    return null;
  }

  @Override
  public boolean isVector(@NotNull final String path) {
    return false;
  }

  @Nullable
  @Override
  public OfflinePlayer getOfflinePlayer(@NotNull final String path) {
    return null;
  }

  @Nullable
  @Override
  public OfflinePlayer getOfflinePlayer(@NotNull final String path, @Nullable final OfflinePlayer def) {
    return null;
  }

  @Override
  public boolean isOfflinePlayer(@NotNull final String path) {
    return false;
  }

  @Nullable
  @Override
  public ItemStack getItemStack(@NotNull final String path) {
    return null;
  }

  @Nullable
  @Override
  public ItemStack getItemStack(@NotNull final String path, @Nullable final ItemStack def) {
    return null;
  }

  @Override
  public boolean isItemStack(@NotNull final String path) {
    return false;
  }

  @Nullable
  @Override
  public Color getColor(@NotNull final String path) {
    return null;
  }

  @Nullable
  @Override
  public Color getColor(@NotNull final String path, @Nullable final Color def) {
    return null;
  }

  @Override
  public boolean isColor(@NotNull final String path) {
    return false;
  }

  @Nullable
  @Override
  public Location getLocation(@NotNull final String path) {
    return null;
  }

  @Nullable
  @Override
  public Location getLocation(@NotNull final String path, @Nullable final Location def) {
    return null;
  }

  @Override
  public boolean isLocation(@NotNull final String path) {
    return false;
  }

  @Nullable
  @Override
  public org.bukkit.configuration.ConfigurationSection getConfigurationSection(@NotNull final String path) {
    final var section = this.section.getConfigurationSection(path);
    return section == null ? null : new BukkitConfigurationSection(section);
  }

  @Nullable
  @Override
  public org.bukkit.configuration.ConfigurationSection getDefaultSection() {
    final var section = this.section.getDefaultSection();
    return section == null ? null : new BukkitConfigurationSection(section);
  }

  /**
   * An interface to exclude methods from the configs's configuration section.
   */
  private interface Exclusions {

    org.bukkit.configuration.ConfigurationSection createSection(String path);

    org.bukkit.configuration.ConfigurationSection createSection(String path, Map<?, ?> map);

    org.bukkit.configuration.ConfigurationSection getConfigurationSection(String path);

    org.bukkit.configuration.ConfigurationSection getDefaultSection();

    org.bukkit.configuration.ConfigurationSection getParent();

    Configuration getRoot();
  }
}
