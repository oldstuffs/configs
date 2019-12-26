package io.github.portlek.configs.file;

import io.github.portlek.configs.FileType;
import io.github.portlek.configs.SectionType;
import io.github.portlek.configs.Sendable;
import io.github.portlek.configs.SendableTitle;
import io.github.portlek.configs.annotations.*;
import io.github.portlek.configs.values.BasicReplaceable;
import io.github.portlek.configs.values.BasicSendable;
import io.github.portlek.itemstack.util.XMaterial;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@BasicFile(fileName = "en", fileType = FileType.YAML)
public final class EnMessages {

    @Instance
    public static Error error;

    @Instance
    public static General general;

    @Instance
    public static General.ItemTest itemTest;

    @Section(path = "error")
    public static final class Error {

        @Value
        public String player_not_found = "%prefix% &cPlayer not found! &8(%player_name%)";

    }

    @Section(path = "general")
    public static final class General {

        @Value
        public Sendable reload_complete = new BasicSendable(
            new BasicReplaceable(
                "%prefix% &aReload complete! &8Took (%ms%ms)"
            )
        );

        @Value(
            titleValue = @TitleValue(
                title = "&eExample Title",
                subTitle = "&eExample Sub Title"
            )
        )
        public SendableTitle title_test;

        @Value(
            itemStackValue = @ItemStackValue(
                material = XMaterial.DIAMOND,
                displayName = "&aExample Item Name",
                enchantments = {
                    "DAMAGE_ALL:1",
                    "DAMAGE_ALL:1"
                },
                lore = {
                    "",
                    "&7Example item lore"
                }
            )
        )
        public ItemStack item_test;

        @Section(path = "item-test-section", sectionType = SectionType.ITEM_STACK)
        public static final class ItemTest {

            @Value
            public String material = "DIAMOND";

            @Value
            public int data = 0;

            @Value
            public String display_name = "&aExample Item Name";

            @Value
            public String[] lore = {
                "",
                "&7Example item lore"
            };

            @Value
            public String[] enchantments = {
                "DAMAGE_ALL:1",
                "DAMAGE_ALL:1"
            };

        }

    }

}
