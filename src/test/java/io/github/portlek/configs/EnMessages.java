package io.github.portlek.configs;

import io.github.portlek.configs.annotations.ConfigSection;
import io.github.portlek.configs.annotations.LanguageFile;
import io.github.portlek.configs.annotations.values.EnchantmentValue;
import io.github.portlek.configs.annotations.values.ItemStackValue;
import io.github.portlek.configs.annotations.values.TitleValue;
import io.github.portlek.configs.annotations.values.Value;
import io.github.portlek.itemstack.util.XEnchantment;
import io.github.portlek.itemstack.util.XMaterial;
import org.bukkit.inventory.ItemStack;

@LanguageFile(fileName = "en", fileType = FileType.YAML)
public final class EnMessages {

    @ConfigSection(path = "error")
    public final class Error {

        @Value(stringValue = "%prefix% &cPlayer not found! &8(%player_name%)")
        public String player_not_found;

    }

    @ConfigSection(path = "general")
    public final class General {

        @Value(stringValue = "%prefix% &aReload complete! &8Took (%ms%ms)")
        public Sendable reload_complete;

        @TitleValue(
            title = "&eExample Title", subTitle = "&eExample Sub Title"
        )
        public SendableTitle title_test;

        @ItemStackValue(
            material = XMaterial.DIAMOND,
            displayName = "&aExample Item Name",
            enchantments = {
                @EnchantmentValue(enchantment = XEnchantment.DAMAGE_ALL, level = 1),
                @EnchantmentValue(enchantment = XEnchantment.DAMAGE_ALL, level = 1)
            },
            lore = {
                "",
                "&7Example item lore"
            }
        )
        public ItemStack item_test;

    }

}
