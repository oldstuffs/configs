package io.github.portlek.configs.file;

import io.github.portlek.configs.Linked;
import io.github.portlek.configs.Sendable;
import io.github.portlek.configs.SendableTitle;
import io.github.portlek.configs.annotations.*;
import io.github.portlek.configs.util.ItemBuilder;
import io.github.portlek.configs.values.BasicReplaceable;
import io.github.portlek.configs.values.BasicSendable;
import io.github.portlek.configs.values.BasicSendableTitle;
import io.github.portlek.itemstack.util.XMaterial;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

@LinkedFile(path = "messages", languages = {"en", "tr"})
public final class Messages extends Linked {

    @Instance
    public final Error error = new Error();

    @Instance
    public final General general = new General();

    public Messages(@NotNull Locale locale) {
        super(locale);
    }

    @Section(path = "error")
    public static final class Error {

        @LinkedValue(
            paths = {
                "player-not-found",
                "oyuncu-bulunamadi"
            },
            stringValues = {
                "%prefix% &cPlayer not found! &8(%player_name%)",
                "%prefix% &cOyuncu bulunamadi! &8(%player_name%)"
            }
        )
        public String player_not_found;

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

        @Instance
        public ItemTest itemTest = new ItemTest();

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
