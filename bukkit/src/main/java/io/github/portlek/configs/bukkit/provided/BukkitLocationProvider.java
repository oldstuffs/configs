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

package io.github.portlek.configs.bukkit.provided;

import io.github.portlek.configs.provided.Provided;
import io.github.portlek.configs.structure.managed.section.CfgSection;
import io.github.portlek.configs.util.GeneralUtilities;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public final class BukkitLocationProvider implements Provided<Location> {

    @Override
    public void set(@NotNull final Location location, @NotNull final CfgSection section, @NotNull final String path) {
        final String finalpath = GeneralUtilities.putDot(path);
        section.set(finalpath + 'x', location.getX());
        section.set(finalpath + 'y', location.getY());
        section.set(finalpath + 'z', location.getZ());
        section.set(finalpath + "yaw", location.getYaw());
        section.set(finalpath + "pitch", location.getPitch());
    }

    @NotNull
    @Override
    public Optional<Location> get(@NotNull final CfgSection section, @NotNull final String path) {
        final String finalpath = GeneralUtilities.putDot(path);
        final Optional<World> worldOptional = section.getString(finalpath + "world")
            .flatMap(s -> Optional.ofNullable(Bukkit.getWorld(s)));
        final Optional<Double> xOptional = section.getDouble(finalpath + 'x');
        final Optional<Double> yOptional = section.getDouble(finalpath + 'y');
        final Optional<Double> zOptional = section.getDouble(finalpath + 'z');
        final Optional<Float> yawOptional = section.getFloat(finalpath + "yaw");
        final Optional<Float> pitchOptional = section.getFloat(finalpath + "pitch");
        if (!worldOptional.isPresent() || !xOptional.isPresent() || !yOptional.isPresent() || !zOptional.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new Location(
            worldOptional.get(),
            xOptional.get(),
            yOptional.get(),
            zOptional.get(),
            yawOptional.orElse(0.0f),
            pitchOptional.orElse(0.0f)));
    }

}
