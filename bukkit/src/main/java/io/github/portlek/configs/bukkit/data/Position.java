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

package io.github.portlek.configs.bukkit.data;

import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.loaders.DataSerializer;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * a class that represents positions.
 */
@RequiredArgsConstructor
@AllArgsConstructor
public final class Position implements DataSerializer {

  /**
   * the world name.
   */
  @NotNull
  private final String worldName;

  /**
   * the x.
   */
  private final double x;

  /**
   * the y.
   */
  private final double y;

  /**
   * the z.
   */
  private final double z;

  /**
   * the pitch.
   */
  private float pitch;

  /**
   * the yaw.
   */
  private float yaw;

  /**
   * ctor.
   *
   * @param worldName the world name.
   * @param x the x.
   * @param y the y.
   * @param z the z.
   */
  public Position(@NotNull final String worldName, final int x, final int y, final int z) {
    this(worldName, (double) x, y, z);
  }

  /**
   * ctor.
   *
   * @param worldName the world name.
   * @param x the x.
   * @param y the y.
   * @param z the z.
   * @param yaw the yaw.
   * @param pitch the pitch.
   */
  public Position(@NotNull final String worldName, final int x, final int y, final int z, final float yaw,
                  final float pitch) {
    this(worldName, (double) x, y, z, yaw, pitch);
  }

  /**
   * gets the position from the given section.
   *
   * @param section the section to get.
   *
   * @return a position instance at the section path.
   */
  @NotNull
  public static Optional<Position> deserialize(@NotNull final ConfigurationSection section) {
    final var world = section.getString("world");
    if (world == null) {
      return Optional.empty();
    }
    final var x = section.getDouble("x");
    final var y = section.getDouble("y");
    final var z = section.getDouble("z");
    return Optional.of(new Position(world, x, y, z));
  }

  /**
   * obtains the location.
   *
   * @return the location.
   */
  @NotNull
  public Optional<Location> getLocation() {
    return Optional.ofNullable(Bukkit.getWorld(this.worldName))
      .map(world ->
        new Location(world,
          this.x, this.y, this.z, this.yaw, this.pitch));
  }

  /**
   * obtains the location.
   *
   * @return the location.
   *
   * @throws NullPointerException if the currently running server has not a world called {@link #worldName} world.
   */
  @NotNull
  public Location getLocationThrown() {
    return new Location(Objects.requireNonNull(Bukkit.getWorld(this.worldName), "world"),
      this.x, this.y, this.z, this.yaw, this.pitch);
  }

  @Override
  public void serialize(@NotNull final ConfigurationSection section) {
    section.set("world", this.worldName);
    section.set("x", this.x);
    section.set("y", this.y);
    section.set("z", this.z);
  }
}
