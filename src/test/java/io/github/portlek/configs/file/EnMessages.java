package io.github.portlek.configs.file;

import io.github.portlek.configs.FileType;
import io.github.portlek.configs.SectionType;
import io.github.portlek.configs.Sendable;
import io.github.portlek.configs.SendableTitle;
import io.github.portlek.configs.annotations.BasicFile;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.sections.Section;
import io.github.portlek.configs.annotations.values.ItemStackValue;
import io.github.portlek.configs.annotations.values.TitleValue;
import io.github.portlek.configs.annotations.values.Value;
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

        @Value(stringValue = "%prefix% &cPlayer not found! &8(%player_name%)")
        public String player_not_found;

    }

    @Section(path = "general")
    public static final class General {

        @Value(stringValue = "%prefix% &aReload complete! &8Took (%ms%ms)")
        public Sendable reload_complete;

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

            @Value(materialValue = XMaterial.DIAMOND)
            public Material material;

            @Value(intValue = 0)
            public int data;

            @Value(stringValue = "&aExample Item Name")
            public String display_name;

            @Value(stringArrayValue = {
                "",
                "&7Example item lore"
            })
            public List<String> lore;

            @Value(
                enchantmentValue = {
                    "DAMAGE_ALL:1",
                    "DAMAGE_ALL:1"
                }
            )
            public List<Enchantment> enchantments;

        }

    }

}
