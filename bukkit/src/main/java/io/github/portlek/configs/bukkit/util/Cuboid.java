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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public final class Cuboid {

    @Getter
    @NotNull
    private final Location minimumLocation;

    @Getter
    @NotNull
    private final Location maximumLocation;

    @NotNull
    private final World world;

    private final double minX;

    private final double minY;

    private final double minZ;

    private final double maxX;

    private final double maxY;

    private final double maxZ;

    public Cuboid(@NotNull final Location minimumLocation, @NotNull final Location maximumLocation) {
        final World minimumWorld = LocationUtil.validWorld(minimumLocation);
        final World maximumWorld = LocationUtil.validWorld(maximumLocation);
        if (!minimumWorld.equals(maximumWorld)) {
            throw new IllegalStateException(minimumWorld + " and " + maximumWorld + " are not equals!");
        }
        this.minimumLocation = minimumLocation;
        this.maximumLocation = maximumLocation;
        this.world = minimumWorld;
        this.minX = Math.min(minimumLocation.getX(), maximumLocation.getX());
        this.minY = Math.min(minimumLocation.getX(), maximumLocation.getX());
        this.minZ = Math.min(minimumLocation.getX(), maximumLocation.getX());
        this.maxX = Math.max(minimumLocation.getX(), maximumLocation.getX());
        this.maxY = Math.max(minimumLocation.getX(), maximumLocation.getX());
        this.maxZ = Math.max(minimumLocation.getX(), maximumLocation.getX());
    }

    private static void remove(@NotNull final Block block) {
        block.setType(Material.AIR);
    }

    @NotNull
    public List<Block> blocks() {
        final List<Block> result = new ArrayList<>();
        for (double x = this.minX; x <= this.maxX; ++x) {
            for (double y = this.minY; y <= this.maxY; ++y) {
                for (double z = this.minZ; z <= this.maxZ; ++z) {
                    result.add(this.world.getBlockAt(new Location(this.world, x, y, z)));
                }
            }
        }
        return result;
    }

    public void set(@NotNull final Material material) {
        this.blocks().forEach(block -> block.setType(material));
    }

    @NotNull
    public Location centerBottom() {
        return new Location(
            this.world,
            this.minX + (this.maxX - this.minX) / 2.0d,
            this.minY,
            this.minZ + (this.maxZ - this.minZ) / 2.0d);
    }

    public boolean isIn(@NotNull final Location location) {
        return location.getX() >= this.minX && location.getX() <= this.maxX &&
            this.minY <= location.getY() && location.getY() <= this.maxY &&
            this.minZ <= location.getZ() && location.getZ() <= this.maxZ;
    }

    public void removeAll() {
        this.blocks().forEach(Cuboid::remove);
    }

    @NotNull
    public List<Block> randomBlocks(final int limit, final boolean duplicate) {
        return RandomUtils.chooseRandoms(this.blocks(), limit, duplicate);
    }

    @NotNull
    public List<Location> randomLocations(final int limit, final boolean duplicate) {
        return this.randomBlocks(limit, duplicate).stream()
            .map(Block::getLocation)
            .collect(Collectors.toList());
    }

}
