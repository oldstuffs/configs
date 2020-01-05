package io.github.portlek.configs.file;

import io.github.portlek.configs.FileType;
import io.github.portlek.configs.Sendable;
import io.github.portlek.configs.SendableTitle;
import io.github.portlek.configs.annotations.BasicFile;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.annotations.Value;
import io.github.portlek.configs.util.ItemBuilder;
import io.github.portlek.configs.values.BasicReplaceable;
import io.github.portlek.configs.values.BasicSendable;
import io.github.portlek.configs.values.BasicSendableTitle;
import io.github.portlek.itemstack.util.XMaterial;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@BasicFile(fileName = "en", fileType = FileType.YAML)
public final class EnMessages {

    @Instance
    public Error error = new Error();

    @Instance
    public General general = new General();

    @Section(path = "error")
    public static final class Error {

        @Value
        @NotNull
        public String player_not_found = "%prefix% &cPlayer not found! &8(%player_name%)";

    }

    @Section(path = "general")
    public static final class General {

        @Instance
        public ItemTest itemTest;

        @Value
        @NotNull
        public Sendable reload_complete = new BasicSendable(
            new BasicReplaceable(
                "%prefix% &aReload complete! &8Took (%ms%ms)"
            )
        );

        @Value
        public SendableTitle title_test = new BasicSendableTitle(
            new BasicReplaceable("&eExample Title"),
            new BasicReplaceable("&eExample Sub Title"),
            20,
            20,
            20
        );

        @Value
        public ItemStack item_test = ItemBuilder.of(XMaterial.DIAMOND)
            .name("&aExample Item Name")
            .data(0)
            .lore(
                "",
                "&7Example item lore"
            ).enchantments(
                "DAMAGE_ALL:1",
                "DAMAGE_ALL:1"
            );

        @Section(path = "item-test-section")
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
            public String[] lore = {"", "&7Example item lore"};

            @Value
            public String[] enchantments = {"DAMAGE_ALL:1", "DAMAGE_ALL:1"};

        }

    }

}
