package io.github.portlek.configs.bukkit.util;

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

public final class ItemBuilder extends ItemStack {

    private ItemBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
    }

    public static ItemBuilder of(@NotNull XMaterial xMaterial) {
        final Material material = xMaterial.parseMaterial();

        if (material == null) {
            throw new IllegalStateException("Material of the " + xMaterial.name() + " cannot be null!");
        }

        return new ItemBuilder(
            new ItemStack(material)
        );
    }

    public static ItemBuilder of(@NotNull Material material) {
        return new ItemBuilder(
            new ItemStack(material)
        );
    }

    public static ItemBuilder of(@NotNull ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public ItemBuilder name(@NotNull String displayName) {
        return name(displayName, true);
    }

    public ItemBuilder name(@NotNull String displayName, boolean colored) {
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

    public ItemBuilder data(int data) {
        return data((byte) data);
    }

    public ItemBuilder data(byte data) {
        final MaterialData materialData = getData();

        materialData.setData(data);
        setData(materialData);

        return this;
    }

    public ItemBuilder lore(@NotNull String... lore) {
        return lore(
            new ListOf<>(
                lore
            ),
            true
        );
    }

    public ItemBuilder lore(@NotNull List<String> lore, boolean colored) {
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

    public ItemBuilder enchantments(@NotNull String... enchantments) {
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

    public ItemBuilder enchantments(@NotNull XEnchantment enchantment, int level) {
        Optional.ofNullable(enchantment.parseEnchantment()).ifPresent(enchant ->
            addUnsafeEnchantments(
                new MapOf<>(
                    new MapEntry<>(enchant, level)
                )
            )
        );

        return this;
    }

    public ItemBuilder enchantments(@NotNull Enchantment enchantment, int level) {
        addUnsafeEnchantments(
            new MapOf<>(
                new MapEntry<>(enchantment, level)
            )
        );

        return this;
    }

    public ItemBuilder enchantments(@NotNull Map<Enchantment, Integer> enchantments) {
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
