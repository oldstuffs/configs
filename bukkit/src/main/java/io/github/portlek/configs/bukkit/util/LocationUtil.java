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

package io.github.portlek.configs.bukkit.util;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class LocationUtil {

    private final Pattern PATTERN = Pattern.compile(
        "(?<world>[^/]+):(?<x>[\\-0-9.]+),(?<y>[\\-0-9.]+),(?<z>[\\-0-9.]+)(:(?<yaw>[\\-0-9.]+):(?<pitch>[\\-0-9.]+))?"
    );

    @NotNull
    public Directions directionOf(@NotNull final Player player) {
        return LocationUtil.directionOf(player.getLocation());
    }

    @NotNull
    public Directions directionOf(@NotNull final Location location) {
        return LocationUtil.directionOf(location.getYaw());
    }

    @NotNull
    public Directions directionOf(final float yaw) {
        return LocationUtil.directionOf(
            Arrays.asList(Directions.SOUTH, Directions.WEST, Directions.NORTH, Directions.EAST),
            yaw);
    }

    @NotNull
    public Directions directionOf(@NotNull final List<Directions> directions, final float yaw) {
        return directions.get((int) (yaw + 45.0f) % 360 / 90);
    }

    @NotNull
    public Directions doubleDirectionOf(@NotNull final Player player) {
        return LocationUtil.directionOf(player.getLocation());
    }

    @NotNull
    public Directions doubleDirectionOf(@NotNull final Location location) {
        return LocationUtil.directionOf(location.getYaw());
    }

    @NotNull
    public Directions doubleDirectionOf(final float yaw) {
        return LocationUtil.directionOf(
            Arrays.asList(
                Directions.SOUTH,
                Directions.SOUTHWEST,
                Directions.WEST,
                Directions.NORTHWEST,
                Directions.NORTH,
                Directions.NORTHEAST,
                Directions.EAST,
                Directions.SOUTHEAST),
            yaw);
    }

    @NotNull
    public Directions doubleDirectionOf(@NotNull final List<Directions> directions, final float yaw) {
        return directions.get((int) (yaw + 22.5f) % 360 / 45);
    }

    @NotNull
    public Location centeredOn(@NotNull final Location location) {
        return LocationUtil.centered(location, 0.1d);
    }

    @NotNull
    public Location centeredIn(@NotNull final Location location) {
        return LocationUtil.centered(location, 0.5d);
    }

    @NotNull
    public World validWorld(@NotNull final Location location) {
        return Optional.ofNullable(location.getWorld()).orElseThrow(() ->
            new IllegalStateException("World of the location cannot be null!"));
    }

    @NotNull
    public Optional<Location> fromKey(@NotNull final String key) {
        final Matcher match = LocationUtil.PATTERN.matcher(key
            .replace("_", ".")
            .replace("/", ":"));
        if (match.matches()) {
            final World world = Bukkit.getWorld(match.group("world"));
            final double x = NumberConversions.toDouble(match.group("x"));
            final double y = NumberConversions.toDouble(match.group("y"));
            final double z = NumberConversions.toDouble(match.group("z"));
            final Float yaw = Optional.ofNullable(match.group("yaw"))
                .map(NumberConversions::toFloat)
                .orElse(0.0f);
            final Float pitch = Optional.ofNullable(match.group("pitch"))
                .map(NumberConversions::toFloat)
                .orElse(0.0f);
            return Optional.of(new Location(world, x, y, z, yaw, pitch));
        }

        return Optional.empty();
    }

    @NotNull
    public String toKey(@NotNull final Location location) {
        final World world = LocationUtil.validWorld(location);
        String s = world.getName() + ':';
        s += String.format(
            Locale.ENGLISH,
            "%.2f,%.2f,%.2f",
            location.getX(), location.getY(), location.getZ());
        if (location.getYaw() != 0.0f || location.getPitch() != 0.0f) {
            s += String.format(
                Locale.ENGLISH,
                ":%.2f:%.2f",
                location.getYaw(), location.getPitch());
        }
        return s.replace(":", "/")
            .replace("\\.", "_");
    }

    @NotNull
    public Vector rotateAroundAxisX(@NotNull final Vector vector, final double angle) {
        final double cos = StrictMath.cos(angle);
        final double sin = StrictMath.sin(angle);
        return vector
            .setY(vector.getY() * cos - vector.getZ() * sin)
            .setZ(vector.getY() * sin + vector.getZ() * cos);
    }

    @NotNull
    public Vector rotateAroundAxisY(@NotNull final Vector vector, final double angle) {
        final double cos = StrictMath.cos(angle);
        final double sin = StrictMath.sin(angle);
        return vector
            .setX(vector.getX() * cos + vector.getZ() * sin)
            .setZ(vector.getX() * -sin + vector.getZ() * cos);
    }

    @NotNull
    public Vector rotateAroundAxisZ(@NotNull final Vector vector, final double angle) {
        final double cos = StrictMath.cos(angle);
        final double sin = StrictMath.sin(angle);
        return vector
            .setX(vector.getX() * cos - vector.getY() * sin)
            .setY(vector.getX() * sin + vector.getY() * cos);
    }

    @NotNull
    public Vector rotateVector(@NotNull final Vector vector, final double angleX, final double angleY, final double angleZ) {
        LocationUtil.rotateAroundAxisX(vector, angleX);
        LocationUtil.rotateAroundAxisY(vector, angleY);
        LocationUtil.rotateAroundAxisZ(vector, angleZ);
        return vector;
    }

    @NotNull
    public Vector rotateVector(@NotNull final Vector vector, @NotNull final Location loc) {
        return LocationUtil.rotateVector(vector, loc.getYaw(), loc.getPitch());
    }

    @NotNull
    public Vector rotateVector(@NotNull final Vector vector, final Float yawDegrees, final Float pitchDegrees) {
        final double yaw = Math.toRadians(-1.0d * (yawDegrees.doubleValue() + 90.0d));
        final double pitch = Math.toRadians(-pitchDegrees.doubleValue());
        final double cosYaw = StrictMath.cos(yaw);
        final double cosPitch = StrictMath.cos(pitch);
        final double sinYaw = StrictMath.sin(yaw);
        final double sinPitch = StrictMath.sin(pitch);
        final double initialX = vector.getX() * cosPitch - vector.getY() * sinPitch;
        final double initialY = vector.getY();
        final double initialZ = vector.getZ();
        final double x = initialX * cosPitch - initialY * sinPitch;
        return new Vector(
            initialZ * sinYaw + x * cosYaw,
            initialX * sinPitch + initialY * cosPitch,
            initialZ * cosYaw - x * sinYaw
        );
    }

    @NotNull
    private Location centered(@NotNull final Location location, final double defaultvalue) {
        final World world = LocationUtil.validWorld(location);
        return new Location(world,
            location.getX() + 0.5d,
            location.getY() + defaultvalue,
            location.getZ() + 0.5d,
            location.getYaw(),
            location.getPitch());
    }

}
