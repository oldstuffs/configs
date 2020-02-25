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

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import java.util.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

public final class BukkitItemBuilder {

    @NotNull
    private final ItemStack itemStack;

    private BukkitItemBuilder(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @NotNull
    public static BukkitItemBuilder of(@NotNull ItemStack itemStack) {
        return new BukkitItemBuilder(itemStack);
    }

    @NotNull
    public static BukkitItemBuilder of(@NotNull Material material) {
        return of(new ItemStack(material));
    }

    @NotNull
    public static BukkitItemBuilder of(@NotNull XMaterial xMaterial) {
        return of(
            Optional.ofNullable(xMaterial.parseMaterial()).orElseThrow(() ->
                new IllegalStateException("Material of the " + xMaterial.name() + " cannot be null!")
            )
        );
    }

    @NotNull
    public BukkitItemBuilder name(@NotNull String displayName) {
        return name(displayName, true);
    }

    @NotNull
    public BukkitItemBuilder name(@NotNull String displayName, boolean colored) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return this;
        }
        if (colored) {
            itemMeta.setDisplayName(
                ColorUtil.colored(displayName)
            );
        } else {
            itemMeta.setDisplayName(displayName);
        }
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    @NotNull
    public BukkitItemBuilder data(int data) {
        return data((byte) data);
    }

    @NotNull
    public BukkitItemBuilder data(byte data) {
        final MaterialData materialData = itemStack.getData();
        materialData.setData(data);
        itemStack.setData(materialData);
        return this;
    }

    @NotNull
    public BukkitItemBuilder lore(@NotNull String... lore) {
        return lore(Arrays.asList(lore), true);
    }

    @NotNull
    public BukkitItemBuilder lore(@NotNull List<String> lore, boolean colored) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return this;
        }
        if (colored) {
            itemMeta.setLore(
                ColorUtil.colored(lore)
            );
        } else {
            itemMeta.setLore(
                lore
            );
        }
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    @NotNull
    public BukkitItemBuilder enchantments(@NotNull String... enchantments) {
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
            XEnchantment.matchXEnchantment(enchantment).ifPresent(xEnchantment -> enchantments(xEnchantment, level));
        }
        return this;
    }

    @NotNull
    public BukkitItemBuilder enchantments(@NotNull XEnchantment enchantment, int level) {
        final Optional<Enchantment> enchantmentOptional = Optional.ofNullable(enchantment.parseEnchantment());
        if (enchantmentOptional.isPresent()) {
            return enchantments(enchantmentOptional.get(), level);
        }
        return this;
    }

    @NotNull
    public BukkitItemBuilder enchantments(@NotNull Enchantment enchantment, int level) {
        final Map<Enchantment, Integer> map = new HashMap<>();
        map.put(enchantment, level);
        return enchantments(map);
    }

    @NotNull
    public BukkitItemBuilder enchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        itemStack.addUnsafeEnchantments(enchantments);
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

    @NotNull
    public ItemStack build() {
        return this.itemStack;
    }
}
