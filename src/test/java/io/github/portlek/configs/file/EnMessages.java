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
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@BasicFile(fileName = "en", fileType = FileType.YAML)
public final class EnMessages {

    @Instance
    public Error error;

    @Instance
    public General general;

    @Instance
    public General.ItemTest itemTest;

    @Section(path = "error")
    public static final class Error {

        @Value
        @NotNull
        public String player_not_found = "%prefix% &cPlayer not found! &8(%player_name%)";

    }

    @Section(path = "general")
    public static final class General {

        @Value
        @NotNull
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
            @NotNull
            public String material = "DIAMOND";

            @Value
            public int data = 0;

            @Value
            @NotNull
            public String display_name = "&aExample Item Name";

            @Value
            @NotNull
            public List<String> lore = Arrays.asList(
                "",
                "&7Example item lore"
            );

            @Value
            public List<String> enchantments = Arrays.asList(
                "DAMAGE_ALL:1",
                "DAMAGE_ALL:1"
            );

        }

    }

}
