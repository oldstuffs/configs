/*
 * MIT License
 *
 * Copyright (c) 2019 Hasan Demirtaş
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

package io.github.portlek.configs.util;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.itemstack.util.ColoredList;
import io.github.portlek.itemstack.util.XEnchantment;
import io.github.portlek.itemstack.util.XMaterial;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.cactoos.list.ListOf;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class BukkitItemBuilder extends ItemStack {

    private BukkitItemBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
    }

    public static BukkitItemBuilder of(@NotNull XMaterial xMaterial) {
        final Material material = xMaterial.parseMaterial();

        if (material == null) {
            throw new IllegalStateException("Material of the " + xMaterial.name() + " cannot be null!");
        }

        return new BukkitItemBuilder(
            new ItemStack(material)
        );
    }

    public static BukkitItemBuilder of(@NotNull Material material) {
        return new BukkitItemBuilder(
            new ItemStack(material)
        );
    }

    public static BukkitItemBuilder of(@NotNull ItemStack itemStack) {
        return new BukkitItemBuilder(itemStack);
    }

    public BukkitItemBuilder name(@NotNull String displayName) {
        return name(displayName, true);
    }

    public BukkitItemBuilder name(@NotNull String displayName, boolean colored) {
        if (getItemMeta() == null) {
            return this;
        }

        if (colored) {
            getItemMeta().setDisplayName(
                new Colored(
                    displayName
                ).value()
            );
        } else {
            getItemMeta().setDisplayName(displayName);
        }

        return this;
    }

    public BukkitItemBuilder data(int data) {
        return data((byte) data);
    }

    public BukkitItemBuilder data(byte data) {
        final MaterialData materialData = getData();

        materialData.setData(data);
        setData(materialData);

        return this;
    }

    public BukkitItemBuilder lore(@NotNull String... lore) {
        return lore(
            new ListOf<>(
                lore
            ),
            true
        );
    }

    public BukkitItemBuilder lore(@NotNull List<String> lore, boolean colored) {
        if (getItemMeta() == null) {
            return this;
        }

        if (colored) {
            getItemMeta().setLore(
                new ColoredList(
                    lore
                ).value()
            );
        } else {
            getItemMeta().setLore(
                lore
            );
        }

        return this;
    }

    public BukkitItemBuilder enchantments(@NotNull String... enchantments) {
        final Map<Enchantment, Integer> enchantmentLevelMap = new HashMap<>();

        for (String s : enchantments) {
            final String[] split = s.split(":");
            final String enchantment;
            final int level;

            if (split.length == 1) {
                enchantment = split[0];
                level = 1;
            } else {
                enchantment = split[0];
                level = getInt(split[1]);
            }

            XEnchantment.matchXEnchantment(enchantment).flatMap(xEnchantment ->
                Optional.ofNullable(xEnchantment.parseEnchantment())
            ).ifPresent(enchant ->
                enchantmentLevelMap.put(enchant, level)
            );
        }

        return enchantments(enchantmentLevelMap);
    }

    public BukkitItemBuilder enchantments(@NotNull XEnchantment enchantment, int level) {
        Optional.ofNullable(enchantment.parseEnchantment()).ifPresent(enchant ->
            addUnsafeEnchantments(
                new MapOf<>(
                    new MapEntry<>(enchant, level)
                )
            )
        );

        return this;
    }

    public BukkitItemBuilder enchantments(@NotNull Enchantment enchantment, int level) {
        addUnsafeEnchantments(
            new MapOf<>(
                new MapEntry<>(enchantment, level)
            )
        );

        return this;
    }

    public BukkitItemBuilder enchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        addUnsafeEnchantments(enchantments);

        return this;
    }

    private int getInt(@NotNull String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception ignored) {
            // ignored
        }

        return 0;
    }

}
